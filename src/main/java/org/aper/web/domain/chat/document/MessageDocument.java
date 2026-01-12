package org.aper.web.domain.chat.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aper.web.domain.chat.dto.ChatWebSocketDto;
import org.aper.web.domain.chat.entity.constant.MessageType;
import org.aper.web.domain.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "messages")
@CompoundIndex(
        name = "room_created_idx",
        def = "{'chatRoomId': 1, 'createdAt': -1}"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDocument {

    @Id
    private String id;

    @Indexed
    @Field("chatRoomId")
    private Long chatRoomId;

    @Field("senderId")
    private Long senderId;

    @Field("senderName")
    private String senderName;

    @Field("senderImage")
    private String senderImage;

    @Field("content")
    private String content;

    @Field("type")
    private MessageType type;

    @Field("isDeleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updatedAt")
    private LocalDateTime updatedAt;

    @Field("payload")
    private Map<String, Object> payload;

    @Field("messageSequence")
    private Long messageSequence;

    public MessageDocument(Long roomId, User user, ChatWebSocketDto.SendMessageRequest request, Long nextSequence) {
        LocalDateTime now = LocalDateTime.now();
        MessageDocument.builder()
                .chatRoomId(roomId)
                .senderId(user.getUserId())
                .senderName(user.getPenName())
                .senderImage(user.getFieldImage())
                .content(request.content())
                .type(request.type())
                .payload(request.payload())
                .isDeleted(false)
                .messageSequence(nextSequence)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}