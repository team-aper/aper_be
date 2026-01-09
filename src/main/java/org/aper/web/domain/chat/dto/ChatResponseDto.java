package org.aper.web.domain.chat.dto;

import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.entity.ChatRoomMember;
import org.aper.web.domain.chat.entity.Message;
import org.aper.web.domain.chat.entity.constant.ChatRoomType;
import org.aper.web.domain.chat.entity.constant.MessageType;
import org.aper.web.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            List<String> memberNames,
            String lastMessage,
            LocalDateTime lastMessageAt,
            Integer unreadCount
    ) {
        public static ChatRoomResponseDto from(ChatRoom chatRoom, Message message, Integer unreadCount, Long userId) {

            // TODO : 근데 이런 비즈니스 로직을 여기에다가 넣는게 적합할까 / 근데 밖에 넣으면 코드가 너무 지저분해질 것 같은데
            List<String> memberNames = new ArrayList<>();

            for (ChatRoomMember member : chatRoom.getMembers()) {
                User user = member.getUser();

                if (!user.getUserId().equals(userId)) memberNames.add(member.getUser().getPenName());
            }

            return new ChatRoomResponseDto(
                    chatRoom.getId(),
                    chatRoom.getName(),
                    chatRoom.getType(),
                    chatRoom.getMemberCount(),
                    memberNames,
                    message != null ? message.getContent() : "",
                    message != null ? message.getCreatedAt() : null,
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
}
