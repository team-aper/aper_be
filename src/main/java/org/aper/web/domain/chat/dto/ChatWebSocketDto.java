package org.aper.web.domain.chat.dto;

import org.aper.web.domain.chat.entity.constant.MessageType;

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
    ) {}

    public record MarkAsReadRequest(
            Long lastReadMessageId
    ) {}

    public record LessonActionRequest(
            String action
    ) {}
}
