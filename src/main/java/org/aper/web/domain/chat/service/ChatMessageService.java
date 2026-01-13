package org.aper.web.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.aper.web.domain.chat.document.LastMessageInfo;
import org.aper.web.domain.chat.document.MessageDocument;
import org.aper.web.domain.chat.document.UserReadTrackingDocument;
import org.aper.web.domain.chat.dto.response.MessageResponse;
import org.aper.web.domain.chat.dto.ChatWebSocketDto;
import org.aper.web.domain.chat.dto.ChatWebSocketDto.MarkAsReadRequest;
import org.aper.web.domain.chat.dto.ChatWebSocketDto.SendMessageRequest;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.entity.constant.MessageType;
import org.aper.web.domain.chat.mapper.MessageMapper;
import org.aper.web.domain.chat.repository.ChatRoomMemberRepository;
import org.aper.web.domain.chat.repository.ChatRoomSummaryDocumentRepository;
import org.aper.web.domain.chat.repository.MessageDocumentRepository;
import org.aper.web.domain.chat.repository.UserReadTrackingDocumentRepository;
import org.aper.web.domain.chat.validator.ChatRoomAccessValidator;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatMessageService {

    private final MessageDocumentRepository messageDocumentRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomSummaryDocumentRepository chatRoomSummaryDocumentRepository;
    private final UserReadTrackingDocumentRepository userReadTrackingDocumentRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UnreadCountCacheService unreadCountCacheService;
    
    private final ChatRoomAccessValidator accessValidator;
    private final MessageMapper messageMapper;


    public void sendMessage(Long roomId, Long userId, SendMessageRequest request) {
        ChatRoomAccessValidator.ValidationResult validated = accessValidator.validate(roomId, userId);
        ChatRoom chatRoom = validated.getChatRoom();
        User sender = validated.getUser();

        MessageContext context = createMessage(chatRoom, sender, request);

        updateChatRoomSummary(context);
        increaseUnreadCounts(context);
        broadcastMessage(context);
    }

    private MessageContext createMessage(ChatRoom chatRoom, User sender, SendMessageRequest request) {
        ChatRoomSummaryDocument summary = chatRoomSummaryDocumentRepository.findByChatRoomId(chatRoom.getId())
                .orElseGet(() -> chatRoomSummaryDocumentRepository.save(new ChatRoomSummaryDocument(chatRoom.getId())));

        Long nextSequence = summary.getCurrentSequence() + 1;
        MessageDocument message = messageDocumentRepository.save(
                new MessageDocument(chatRoom.getId(), sender, request, nextSequence)
        );

        log.debug("MongoDB에 메시지 저장 완료 - id : {}, sequence: {}", message.getId(), nextSequence);

        return new MessageContext(chatRoom, sender, request, summary, message, nextSequence);
    }

    private void updateChatRoomSummary(MessageContext context) {
        ChatRoomSummaryDocument summary = context.getSummary();
        MessageDocument message = context.getMessage();
        User sender = context.getSender();

        summary.setCurrentSequence(context.getSequence());
        summary.setLastMessage(new LastMessageInfo(message.getId(), sender, context.getRequest()));
        summary.setUpdatedAt(LocalDateTime.now());

        chatRoomSummaryDocumentRepository.save(summary);
    }

    private void increaseUnreadCounts(MessageContext context) {
        List<Long> memberIds = chatRoomMemberRepository
                .findMemberIdsByChatRoom(context.getChatRoom());

        unreadCountCacheService.incrementUnreadCountForMembers(context.getRoomId(), memberIds,
                context.getSender().getUserId());
    }

    private void broadcastMessage(MessageContext context) {
        MessageDocument message = context.getMessage();
        User sender = context.getSender();

        ChatWebSocketDto.MessageResponse response = ChatWebSocketDto.MessageResponse.from(message, context.getRoomId(), sender, context.getRequest());

        messagingTemplate.convertAndSend("/topic/chatroom/" + context.getRoomId(), response);

        log.info("Message sent - roomId: {}, messageId: {}, sequence: {}",
                context.getRoomId(),
                message.getId(),
                context.getSequence());
    }


    public void markAsRead(Long roomId, Long userId, MarkAsReadRequest request) {
        accessValidator.validate(roomId, userId);

        UserReadTrackingDocument tracking = userReadTrackingDocumentRepository
                .findByUserIdAndChatRoomId(userId, roomId)
                .orElseGet(() -> UserReadTrackingDocument.create(userId, roomId));

        tracking.markAsRead(request.lastReadMessageId(), request.lastReadSequence());
        userReadTrackingDocumentRepository.save(tracking); // mongodb

        unreadCountCacheService.resetUnreadCount(roomId, userId); // redis

        log.info("Marked as read - roomId: {}, userId: {}, sequence: {}", roomId, userId, request.lastReadSequence());
    }


    public void handleLessonAction(Long roomId, Long userId, String action) {
        accessValidator.validate(roomId, userId);

        // 시스템 메시지 생성 및 전송
        SendMessageRequest systemRequest = createSystemMessage(action, userId);
        sendMessage(roomId, userId, systemRequest);

        log.info("Lesson action completed - roomId: {}, userId: {}, action: {}", roomId, userId, action);
    }

    private SendMessageRequest createSystemMessage(String action, Long userId) {
        return switch (action.toLowerCase()) {
            case "accept" -> new SendMessageRequest(
                    "수업 요청이 승인되었습니다.",
                    MessageType.LESSON_ACCEPTED,
                    Map.of("action", "accept", "actionBy", userId)
            );
            case "reject" -> new SendMessageRequest(
                    "수업 요청이 거절되었습니다.",
                    MessageType.LESSON_REJECTED,
                    Map.of("action", "reject", "actionBy", userId)
            );
            default -> throw new ServiceException(ErrorCode.INVALID_LESSON_ACTION);
        };
    }


    @Transactional(readOnly = true)
    public Slice<MessageResponse.MessageDocumentDto> getMessages(Long roomId, Long userId, Pageable pageable) {
        accessValidator.validate(roomId, userId);

        Slice<MessageDocument> messages = messageDocumentRepository
                .findByChatRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(roomId, pageable);

        List<MessageResponse.MessageDocumentDto> content = messageMapper.toDtoList(messages.getContent());

        return new SliceImpl<>(content, pageable, messages.hasNext());
    }

    // 특정 시간 이후의 메시지 조회 : 동기화용
    @Transactional(readOnly = true)
    public List<MessageResponse.MessageDocumentDto> getMessagesSince(Long roomId, Long userId, LocalDateTime since) {
        accessValidator.validate(roomId, userId);

        Slice<MessageDocument> messages = messageDocumentRepository
                .findByChatRoomIdAndCreatedAtAfterAndIsDeletedFalseOrderByCreatedAtDesc(
                        roomId, since, Pageable.unpaged());

        return messageMapper.toDtoList(messages.getContent());
    }
}