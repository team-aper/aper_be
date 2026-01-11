package org.aper.web.domain.chat.entity;

import org.aper.web.domain.chat.entity.constant.ChatRoomType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ChatRoomEntity")
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roomId;  // UUID or custom ID

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ChatRoomType type;  // DIRECT, GROUP, CHANNEL

    @OneToMany(mappedBy = "chatRoom", cascade =
            CascadeType.ALL)
    private List<ChatRoomMember> members = new
            ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages = new ArrayList<>();

    @Column
    private LocalDateTime lastMessageAt;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "last_message_id")
    private Message lastMessage;

    private Integer memberCount;

    @Builder
    private ChatRoom(Long id, String roomId, String name, ChatRoomType type, List<ChatRoomMember> members,
                     List<Message> messages, LocalDateTime lastMessageAt, Integer memberCount) {
        this.id = id;
        this.roomId = roomId;
        this.name = name;
        this.type = type;
        this.members = members;
        this.messages = messages;
        this.lastMessageAt = lastMessageAt;
        this.memberCount = memberCount;
    }
}
