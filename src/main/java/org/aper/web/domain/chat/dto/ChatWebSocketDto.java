package org.aper.web.domain.chat.dto;

import org.aper.web.domain.chat.document.MessageDocument;
import org.aper.web.domain.chat.entity.constant.MessageType;
import org.aper.web.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.Map;

public class ChatWebSocketDto {

    public record SendMessageRequest(
            String content,
            MessageType type,
            Map<String, Object> payload
    ) {}

    public record MessageResponse(
            String messageId,
            Long chatRoomId,
            Long senderId,
            String senderName,
            String senderImage,
            String content,
            MessageType type,
            Map<String, Object> payload,
            Integer unreadCount,
            LocalDateTime createdAt
    ) {
        public static MessageResponse from(MessageDocument message, Long roomId, User sender, SendMessageRequest request) {
            return new MessageResponse(
                    message.getId(),
                    roomId,
                    sender.getUserId(),
                    sender.getPenName(),
                    sender.getFieldImage(),
                    request.content(),
                    request.type(),
                    request.payload(),
                    null,
                    message.getCreatedAt()
            );
        }
    }

    public record MarkAsReadRequest(
            String lastReadMessageId,  // MongoDB _id는 String
            Long lastReadSequence
    ) {}

    public record LessonActionRequest(
            String action
    ) {}
}