package org.aper.web.domain.chat.document;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.aper.web.domain.chat.dto.ChatWebSocketDto;
import org.aper.web.domain.chat.entity.constant.MessageType;
import org.aper.web.domain.user.entity.User;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection="messages")
@CompoundIndex(
        name = "room_created_idx",
        def = "{'chatRoomId': 1, 'createdAt': -1, '_id': -1}"
)
@Data
@Builder(access = AccessLevel.PRIVATE)
public class MessageDocument {
    @Id
    private String id;
    private Long chatRoomId;

    private Long senderId;
    private String senderName;
    private String senderImage;

    private String content;
    private MessageType type;

    private Boolean isDeleted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Map<String, Object> payload;

    public static MessageDocument of(Long roomId, User user, ChatWebSocketDto.SendMessageRequest request) {
        return MessageDocument.builder()
                .chatRoomId(roomId)
                .senderId(user.getUserId())
                .senderName(user.getPenName())
                .senderImage(user.getFieldImage())
                .content(request.content())
                .type(request.type())
                .payload(request.payload())
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
