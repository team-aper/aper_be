package org.aper.web.domain.chat.dto.response;

import org.aper.web.domain.chat.dto.response.common.SenderInfo;
import org.aper.web.domain.chat.entity.Message;
import org.aper.web.domain.chat.entity.constant.MessageType;

import java.time.LocalDateTime;
import java.util.Map;


public class MessageResponse {

    public record MessageDto(
            Long messageId,
            String content,
            MessageType type,
            SenderInfo sender,
            LocalDateTime createdAt,
            Integer unreadCount
    ) {
        public static MessageDto from(Message message, Integer unreadCount) {
            SenderInfo senderInfo = new SenderInfo(
                    message.getSenderId(),
                    message.getSenderName(),
                    message.getSenderImage()
            );

            return new MessageDto(
                    message.getId(),
                    message.getContent(),
                    message.getType(),
                    senderInfo,
                    message.getCreatedAt(),
                    unreadCount
            );
        }
    }

    public record MessageDocumentDto(
            String messageId,
            Long chatRoomId,
            SenderInfo sender,
            String content,
            MessageType type,
            Map<String, Object> payload,
            Long messageSequence,
            LocalDateTime createdAt
    ) {
    }
}
