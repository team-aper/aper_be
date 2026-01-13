package org.aper.web.domain.chat.dto.response;

import org.aper.web.domain.chat.document.LastMessageInfo;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.entity.Message;
import org.aper.web.domain.chat.entity.constant.ChatRoomType;

import java.time.LocalDateTime;


public class ChatRoomResponse {

    public record CreatedChatRoomDto(
            Long id,
            String name,
            ChatRoomType type,
            Integer memberCount,
            LocalDateTime createdAt
    ) {
        public static CreatedChatRoomDto from(ChatRoom chatRoom) {
            return new CreatedChatRoomDto(
                    chatRoom.getId(),
                    chatRoom.getName(),
                    chatRoom.getType(),
                    chatRoom.getMemberCount(),
                    chatRoom.getCreatedAt()
            );
        }
    }

    public record ChatRoomDetailDto(
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
        public static ChatRoomDetailDto from(ChatRoom chatRoom, Message message, Integer unreadCount) {
            return new ChatRoomDetailDto(
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

        public static ChatRoomDetailDto fromMongo(
                ChatRoom chatRoom,
                LastMessageInfo lastMessage,
                Integer unreadCount
        ) {
            return new ChatRoomDetailDto(
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


    public record ChatRoomSummaryDto(
            Long roomId,
            String roomName,
            String roomImage,
            String lastMessage,
            LocalDateTime lastMessageTime,
            Integer unreadCount,
            Integer memberCount
    ) {
        public static ChatRoomSummaryDto fromMongo(
                ChatRoom chatRoom,
                LastMessageInfo lastMessage,
                Integer unreadCount
        ) {
            return new ChatRoomSummaryDto(
                    chatRoom.getId(),
                    chatRoom.getName(),
                    null, // roomImage
                    lastMessage != null ? lastMessage.getContent() : "",
                    lastMessage != null ? lastMessage.getCreatedAt() : null,
                    unreadCount != null ? unreadCount : 0,
                    chatRoom.getMemberCount()
            );
        }
    }
}
