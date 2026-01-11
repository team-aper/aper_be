package org.aper.web.domain.chat.entity;

import lombok.*;
import org.aper.web.domain.chat.entity.constant.MessageType;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

@Entity(name = "MessageEntity")
@Table(name = "messages", indexes = {
        @Index(name = "idx_chat_room_created", columnList =
                "chat_room_id, created_at DESC")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("deleted_at IS NULL")
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    private Long senderId;
    private String senderName;
    private String senderImage;

    @Column(name = "mongo_message_id")
    private String mongoMessageId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    // 전체 채팅방에서 해당 메시지를 읽지 않은 사용자 수 (캐싱용, 실제 값은 Redis)
    @Transient
    private Integer unreadCount;

    public static Message create(ChatRoom chatRoom, Long senderId, String senderName,
                                 String senderImage, String mongoMessageId,
                                 String content, MessageType type) {
        return Message.builder()
                .chatRoom(chatRoom)
                .senderId(senderId)
                .senderName(senderName)
                .senderImage(senderImage)
                .mongoMessageId(mongoMessageId)
                .content(content)
                .type(type)
                .isDeleted(false)
                .build();
    }
}
