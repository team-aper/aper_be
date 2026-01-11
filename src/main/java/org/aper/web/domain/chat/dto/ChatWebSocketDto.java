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

    /**
     * 읽음 처리 요청
     */
    public record MarkAsReadRequest(
            String lastReadMessageId,  // MongoDB _id는 String
            Long lastReadSequence
    ) {}

    public record LessonActionRequest(
            String action
    ) {}
}