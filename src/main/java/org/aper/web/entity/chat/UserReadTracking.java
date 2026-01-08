package org.aper.web.entity.chat;

import org.aper.web.entity.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity(name = "UserReadTrackingEntity")
@Table(name = "user_read_tracking",
        uniqueConstraints = @UniqueConstraint(columnNames =
                {"chat_room_id", "user_id"}),
        indexes = {
                @Index(name = "idx_user_chat_room", columnList =
                        "user_id, chat_room_id")
        })
@Getter
public class UserReadTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_read_message_id")
    private Message lastReadMessage;

    private LocalDateTime lastReadAt;

    // 안 읽은 메시지 수 (캐싱용)
    @Column(name = "unread_count")
    private Integer unreadCount = 0;

    // 비즈니스 메서드
    public void updateLastRead(Message message) {
        this.lastReadMessage = message;
        this.lastReadAt = LocalDateTime.now();
        this.unreadCount = 0;
    }
}
