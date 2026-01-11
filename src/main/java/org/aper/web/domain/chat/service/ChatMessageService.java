package org.aper.web.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.aper.web.domain.chat.document.MessageDocument;
import org.aper.web.domain.chat.document.UserReadTrackingDocument;
import org.aper.web.domain.chat.dto.ChatResponseDto;
import org.aper.web.domain.chat.dto.ChatWebSocketDto.*;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.entity.ChatRoomMember;
import org.aper.web.domain.chat.entity.constant.MessageType;
import org.aper.web.domain.chat.repository.*;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatMessageService {

    private final MessageDocumentRepository messageDocumentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomSummaryDocumentRepository chatRoomSummaryDocumentRepository;
    private final UserReadTrackingDocumentRepository userReadTrackingDocumentRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UnreadCountCacheService unreadCountCacheService;

    /**
     * 메시지 전송 - MongoDB + Redis
     */
    public void sendMessage(Long roomId, Long userId, SendMessageRequest request) {
        // 1. 유효성 검증
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        chatRoomMemberRepository.findByChatRoomAndUserUserId(chatRoom, userId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 멤버가 아닙니다"));

        LocalDateTime now = LocalDateTime.now();

        // 2. ChatRoomSummary에서 다음 시퀀스 번호 가져오기
        ChatRoomSummaryDocument summary = chatRoomSummaryDocumentRepository
                .findByChatRoomId(roomId)
                .orElseGet(() -> {
                    ChatRoomSummaryDocument newSummary = ChatRoomSummaryDocument.builder()
                            .chatRoomId(roomId)
                            .currentSequence(0L)
                            .updatedAt(now)
                            .build();
                    return chatRoomSummaryDocumentRepository.save(newSummary);
                });

        Long nextSequence = summary.getCurrentSequence() + 1;

        // 3. MongoDB에 메시지 저장
        MessageDocument document = MessageDocument.builder()
                .chatRoomId(roomId)
                .senderId(userId)
                .senderName(user.getPenName())
                .senderImage(user.getFieldImage())
                .content(request.content())
                .type(request.type())
                .payload(request.payload())
                .isDeleted(false)
                .messageSequence(nextSequence)
                .createdAt(now)
                .updatedAt(now)
                .build();

        MessageDocument savedDocument = messageDocumentRepository.save(document);
        log.debug("MongoDB에 메시지 저장 완료 - id: {}, sequence: {}",
                savedDocument.getId(), nextSequence);

        // 4. ChatRoomSummary 업데이트 (최신 메시지 정보)
        summary.setCurrentSequence(nextSequence);
        summary.setLastMessage(
                ChatRoomSummaryDocument.LastMessageInfo.builder()
                        .messageId(savedDocument.getId())
                        .senderId(userId)
                        .senderName(user.getPenName())
                        .senderImage(user.getFieldImage())
                        .content(request.content())
                        .createdAt(now)
                        .build()
        );
        summary.setUpdatedAt(now);
        chatRoomSummaryDocumentRepository.save(summary);

        // 5. 모든 멤버의 안읽은 개수 증가 (Redis)
        List<ChatRoomMember> members = chatRoomMemberRepository.findByChatRoom(chatRoom);
        List<Long> memberIds = members.stream()
                .map(m -> m.getUser().getUserId())
                .toList();

        unreadCountCacheService.incrementUnreadCountForMembers(roomId, memberIds, userId);

        // 6. WebSocket 브로드캐스트
        int unreadCount = members.size() - 1;
        MessageResponse response = new MessageResponse(
                savedDocument.getId(),
                roomId,
                userId,
                user.getPenName(),
                user.getFieldImage(),
                request.content(),
                request.type(),
                request.payload(),
                unreadCount,
                savedDocument.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, response);

        log.info("Message sent - roomId: {}, messageId: {}, sequence: {}",
                roomId, savedDocument.getId(), nextSequence);
    }

    /**
     * 읽음 처리 - MongoDB + Redis
     */
    public void markAsRead(Long roomId, Long userId, MarkAsReadRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        chatRoomMemberRepository.findByChatRoomAndUserUserId(chatRoom, userId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 멤버가 아닙니다"));

        // 1. MongoDB - UserReadTracking 업데이트
        UserReadTrackingDocument tracking = userReadTrackingDocumentRepository
                .findByUserIdAndChatRoomId(userId, roomId)
                .orElseGet(() -> UserReadTrackingDocument.builder()
                        .userId(userId)
                        .chatRoomId(roomId)
                        .build());

        tracking.setLastReadMessageId(request.lastReadMessageId());
        tracking.setLastReadSequence(request.lastReadSequence());
        tracking.setLastReadAt(LocalDateTime.now());

        userReadTrackingDocumentRepository.save(tracking);

        // 2. Redis - 안읽은 개수 초기화
        unreadCountCacheService.resetUnreadCount(roomId, userId);

        log.info("Mark as read - roomId: {}, userId: {}, sequence: {}",
                roomId, userId, request.lastReadSequence());
    }

    /**
     * 수업 승인/거절
     */
    public void handleLessonAction(Long roomId, Long userId, String action) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        chatRoomMemberRepository.findByChatRoomAndUserUserId(chatRoom, userId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 멤버가 아닙니다"));

        // 시스템 메시지 생성
        String systemMessage;
        MessageType systemType;

        if ("accept".equalsIgnoreCase(action)) {
            systemMessage = "수업 요청이 승인되었습니다.";
            systemType = MessageType.LESSON_ACCEPTED;
        } else if ("reject".equalsIgnoreCase(action)) {
            systemMessage = "수업 요청이 거절되었습니다.";
            systemType = MessageType.LESSON_REJECTED;
        } else {
            throw new IllegalArgumentException("유효하지 않은 액션입니다: " + action);
        }

        // 시스템 메시지 전송
        SendMessageRequest systemRequest = new SendMessageRequest(
                systemMessage,
                systemType,
                Map.of("action", action, "actionBy", userId)
        );

        sendMessage(roomId, userId, systemRequest);

        log.info("Lesson action completed - roomId: {}, userId: {}, action: {}",
                roomId, userId, action);
    }

    /**
     * 채팅방의 메시지 목록 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public Slice<ChatResponseDto.MessageDocumentResponseDto> getMessages(Long roomId, Long userId, Pageable pageable) {
        // 1. 권한 확인
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        chatRoomMemberRepository.findByChatRoomAndUserUserId(chatRoom, userId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 멤버가 아닙니다"));

        // 2. MongoDB에서 메시지 조회 (최신순)
        Slice<MessageDocument> messages = messageDocumentRepository
                .findByChatRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(roomId, pageable);

        // 3. DTO 변환
        List<ChatResponseDto.MessageDocumentResponseDto> content = messages.getContent().stream()
                .map(msg -> new ChatResponseDto.MessageDocumentResponseDto(
                        msg.getId(),
                        msg.getChatRoomId(),
                        new ChatResponseDto.MessageSenderInfo(
                                msg.getSenderId(),
                                msg.getSenderName(),
                                msg.getSenderImage()
                        ),
                        msg.getContent(),
                        msg.getType(),
                        msg.getPayload(),
                        msg.getMessageSequence(),
                        msg.getCreatedAt()
                ))
                .toList();

        return new SliceImpl<>(content, pageable, messages.hasNext());
    }

    /**
     * 특정 시간 이후의 메시지 조회 (실시간 동기화용)
     */
    @Transactional(readOnly = true)
    public List<ChatResponseDto.MessageDocumentResponseDto> getMessagesSince(Long roomId, Long userId, LocalDateTime since) {
        // 1. 권한 확인
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        chatRoomMemberRepository.findByChatRoomAndUserUserId(chatRoom, userId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 멤버가 아닙니다"));

        // 2. 특정 시간 이후 메시지 조회
        Slice<MessageDocument> messages = messageDocumentRepository
                .findByChatRoomIdAndCreatedAtAfterAndIsDeletedFalseOrderByCreatedAtDesc(
                        roomId, since, Pageable.unpaged());

        // 3. DTO 변환
        return messages.getContent().stream()
                .map(msg -> new ChatResponseDto.MessageDocumentResponseDto(
                        msg.getId(),
                        msg.getChatRoomId(),
                        new ChatResponseDto.MessageSenderInfo(
                                msg.getSenderId(),
                                msg.getSenderName(),
                                msg.getSenderImage()
                        ),
                        msg.getContent(),
                        msg.getType(),
                        msg.getPayload(),
                        msg.getMessageSequence(),
                        msg.getCreatedAt()
                ))
                .toList();
    }
}