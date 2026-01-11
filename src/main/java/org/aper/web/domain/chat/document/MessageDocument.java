package org.aper.web.domain.chat.document;

<<<<<<< HEAD
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.aper.web.domain.chat.dto.ChatWebSocketDto;
import org.aper.web.domain.chat.entity.constant.MessageType;
import org.aper.web.domain.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
=======

import lombok.Builder;
import lombok.Data;
import org.aper.web.domain.chat.entity.constant.MessageType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
>>>>>>> 80ce204a29200e7a0a45d6203171bf5b5c8348fd

import java.time.LocalDateTime;
import java.util.Map;

<<<<<<< HEAD
@Document(collection="messages")
@CompoundIndex(
        name = "room_created_idx",
        def = "{'chatRoomId': 1, 'createdAt': -1, '_id': -1}"
)
@Data
@Getter
=======
@Document(collection = "messages")
@CompoundIndex(
        name = "room_created_idx",
        def = "{'chatRoomId':1, 'createdAt':-1, '_id' : -1}"
)
@Data
>>>>>>> 80ce204a29200e7a0a45d6203171bf5b5c8348fd
@Builder
public class MessageDocument {
    @Id
    private String id;
<<<<<<< HEAD

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

    // TODO: 추가된 필드 messageSequence 확인하기
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

=======
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

>>>>>>> 80ce204a29200e7a0a45d6203171bf5b5c8348fd
}
