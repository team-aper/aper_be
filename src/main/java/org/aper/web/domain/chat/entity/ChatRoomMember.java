package org.aper.web.domain.chat.entity;

import org.aper.web.domain.chat.entity.constant.MemberRole;
import org.aper.web.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity(name = "ChatRoomMemberEntity")
@Table(name = "chat_room_members",
        uniqueConstraints = @UniqueConstraint(columnNames =
                {"chat_room_id", "user_id"}))
@Getter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class ChatRoomMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private MemberRole role;  // OWNER, ADMIN, MEMBER

    private LocalDateTime joinedAt;

    private LocalDateTime lastSeenAt;

    // 알림 설정
    private Boolean notificationEnabled = true;

    @Builder(access = AccessLevel.PRIVATE)
    private ChatRoomMember(Long id, ChatRoom chatRoom, User user, MemberRole role, LocalDateTime joinedAt, LocalDateTime lastSeenAt, Boolean notificationEnabled) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.user = user;
        this.role = role;
        this.joinedAt = joinedAt;
        this.lastSeenAt = lastSeenAt;
        this.notificationEnabled = notificationEnabled;
    }

    public static ChatRoomMember create(ChatRoom chatRoom, User user, boolean isOwner) {
        return ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .user(user)
                .role(isOwner ? MemberRole.OWNER : MemberRole.MEMBER)
                .joinedAt(LocalDateTime.now())
                .notificationEnabled(true)
                .build();
    }
}
