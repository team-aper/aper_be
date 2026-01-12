package org.aper.web.domain.chat.dto;

import org.aper.web.domain.chat.document.LastMessageInfo;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.entity.Message;
import org.aper.web.domain.chat.entity.constant.ChatRoomType;
import org.aper.web.domain.chat.entity.constant.MessageType;

import java.time.LocalDateTime;
import java.util.Map;

public class ChatResponseDto {

    public record CreatedChatRoomResponseDto(
            Long id,
            String name,
            ChatRoomType type,
            Integer memberCount,
            LocalDateTime createdAt
    ) {
        public static CreatedChatRoomResponseDto from(ChatRoom chatRoom) {
            return new CreatedChatRoomResponseDto(
                    chatRoom.getId(),
                    chatRoom.getName(),
                    chatRoom.getType(),
                    chatRoom.getMemberCount(),
                    chatRoom.getCreatedAt()
            );
        }
    }

    public record ChatRoomResponseDto(
            Long id,
            String name,
            ChatRoomType type,
            Integer memberCount,
            LocalDateTime createdAt,
            String lastMessage,
            LocalDateTime lastMessageAt,
            String lastMessageSenderName,
            Integer unreadCount
    ) {
        public static ChatRoomResponseDto from(ChatRoom chatRoom, Message message, Integer unreadCount) {
            return new ChatRoomResponseDto(
                    chatRoom.getId(),
                    chatRoom.getName(),
                    chatRoom.getType(),
                    chatRoom.getMemberCount(),
                    chatRoom.getCreatedAt(),
                    message != null ? message.getContent() : "",
                    message != null ? message.getCreatedAt() : null,
                    message != null ? message.getSenderName() : "",
                    unreadCount != null ? unreadCount : 0
            );
        }

        public static ChatRoomResponseDto fromMongo(
                ChatRoom chatRoom,
                LastMessageInfo lastMessage,
                Integer unreadCount
        ) {
            return new ChatRoomResponseDto(
                    chatRoom.getId(),
                    chatRoom.getName(),
                    chatRoom.getType(),
                    chatRoom.getMemberCount(),
                    chatRoom.getCreatedAt(),
                    lastMessage != null ? lastMessage.getContent() : "",
                    lastMessage != null ? lastMessage.getCreatedAt() : null,
                    lastMessage != null ? lastMessage.getSenderName() : "",
                    unreadCount != null ? unreadCount : 0
            );
        }
    }

    public record ChatRoomListResponseDto(
            Long roomId,
            String roomName,
            String roomImage,
            String lastMessage,
            LocalDateTime lastMessageTime,
            Integer unreadCount,
            Integer memberCount
    ) {}

    public record MessageResponseDto(
            Long messageId,
            String content,
            MessageType type,
            SenderInfo sender,
            LocalDateTime createdAt,
            Integer unreadCount
    ) {
        public static MessageResponseDto from(Message message, Integer unreadCount) {
            SenderInfo senderInfo = new SenderInfo(
                    message.getSenderId(),
                    message.getSenderName(),
                    message.getSenderImage()
            );

            return new MessageResponseDto(
                    message.getId(),
                    message.getContent(),
                    message.getType(),
                    senderInfo,
                    message.getCreatedAt(),
                    unreadCount
            );
        }
    }

    public record SenderInfo(
            Long userId,
            String nickname,
            String profileImage
    ) {}

    /**
     * MongoDB 메시지 조회 응답
     */
    public record MessageDocumentResponseDto(
            String messageId,
            Long chatRoomId,
            MessageSenderInfo sender,
            String content,
            MessageType type,
            Map<String, Object> payload,
            Long messageSequence,
            LocalDateTime createdAt
    ) {}

    /**
     * 메시지 발신자 정보
     */
    public record MessageSenderInfo(
            Long userId,
            String nickname,
            String profileImage
    ) {}
}