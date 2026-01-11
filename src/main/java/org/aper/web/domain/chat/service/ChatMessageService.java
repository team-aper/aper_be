package org.aper.web.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.aper.web.domain.chat.document.MessageDocument;
import org.aper.web.domain.chat.document.UserReadTrackingDocument;
import org.aper.web.domain.chat.dto.ChatWebSocketDto.*;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.entity.ChatRoomMember;
import org.aper.web.domain.chat.repository.*;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 메시지 전송 - MongoDB만 사용
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
                .senderName(user.getPenName())  // getName() -> getPenName()
                .senderImage(user.getFieldImage())  // getProfileImage() -> getFieldImage()
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
                        .senderName(user.getPenName())  // getName() -> getPenName()
                        .senderImage(user.getFieldImage())  // getProfileImage() -> getFieldImage()
                        .content(request.content())
                        .createdAt(now)
                        .build()
        );
        summary.setUpdatedAt(now);
        chatRoomSummaryDocumentRepository.save(summary);

        // 5. 안 읽은 사람 수 계산
        List<ChatRoomMember> members = chatRoomMemberRepository.findByChatRoom(chatRoom);
        int unreadCount = members.size() - 1;

        // 6. WebSocket 브로드캐스트
        MessageResponse response = new MessageResponse(
                savedDocument.getId(),
                roomId,
                userId,
                user.getPenName(),  // getName() -> getPenName()
                user.getFieldImage(),  // getProfileImage() -> getFieldImage()
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
     * 읽음 처리 - MongoDB
     */
    public void markAsRead(Long roomId, Long userId, MarkAsReadRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        chatRoomMemberRepository.findByChatRoomAndUserUserId(chatRoom, userId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 멤버가 아닙니다"));

        // UserReadTracking 업데이트
        UserReadTrackingDocument tracking = userReadTrackingDocumentRepository
                .findByUserIdAndChatRoomId(userId, roomId)
                .orElseGet(() -> UserReadTrackingDocument.builder()
                        .userId(userId)
                        .chatRoomId(roomId)
                        .build());

        tracking.setLastReadMessageId(request.lastReadMessageId());  // String으로 수정됨
        tracking.setLastReadSequence(request.lastReadSequence());
        tracking.setLastReadAt(LocalDateTime.now());

        userReadTrackingDocumentRepository.save(tracking);

        log.info("Mark as read - roomId: {}, userId: {}, sequence: {}",
                roomId, userId, request.lastReadSequence());
    }

    /**
     * 수업 승인/거절
     */
    public void handleLessonAction(Long roomId, Long userId, String action) {
        // TODO: Phase 3에서 구현
        log.info("Lesson action - roomId: {}, userId: {}, action: {}",
                roomId, userId, action);
    }
}