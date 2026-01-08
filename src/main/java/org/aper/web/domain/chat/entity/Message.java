package org.aper.web.domain.chat.entity;

import org.aper.web.domain.chat.entity.constant.MessageType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLRestriction;

@Entity(name = "MessageEntity")
@Table(name = "messages", indexes = {
        @Index(name = "idx_chat_room_created", columnList =
                "chat_room_id, created_at DESC")
})
@Getter
@SQLRestriction("deleted_at IS NULL")
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    // TODO: 원래 User 를 참조하면서 받아오는 필드였는데, User의 경우 auth_db에서 주관으로 처리하고 있는 entity이기 때문에
    private Long senderId;
    private String senderName;
    private String senderImage;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;  // TEXT, IMAGE, FILE, SYSTEM

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // 전체 채팅방에서 해당 메시지를 읽지 않은 사용자 수 (캐싱용, 실제 값은 Redis)
    @Transient
    private Integer unreadCount;
}
