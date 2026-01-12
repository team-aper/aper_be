package org.aper.web.domain.chat.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;


@Document(collection = "user_read_tracking")
@CompoundIndex(
        name = "user_room_idx",
        def = "{'userId': 1, 'chatRoomId': 1}",
        unique = true
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReadTrackingDocument {

    @Id
    private String id;

    @Field("userId")
    private Long userId;

    @Field("chatRoomId")
    private Long chatRoomId;

    // 마지막으로 읽은 메시지 ID (MongoDB _id)
    @Field("lastReadMessageId")
    private String lastReadMessageId;

    // 마지막으로 읽은 메시지 번호
    @Field("lastReadSequence")
    private Long lastReadSequence;

    @LastModifiedDate
    @Field("lastReadAt")
    private LocalDateTime lastReadAt;

    public void markAsRead(String messageId, Long sequence) {
        this.lastReadMessageId = messageId;
        this.lastReadSequence = sequence;
        this.lastReadAt = LocalDateTime.now();
    }

    public static UserReadTrackingDocument create(Long userId, Long chatRoomId) {
        return UserReadTrackingDocument.builder()
                .userId(userId)
                .chatRoomId(chatRoomId)
                .lastReadSequence(0L)
                .lastReadAt(LocalDateTime.now())
                .build();
    }
}