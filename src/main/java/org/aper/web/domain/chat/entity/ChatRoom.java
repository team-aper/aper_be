package org.aper.web.domain.chat.entity;

import org.aper.web.domain.chat.entity.constant.ChatRoomType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

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

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ChatRoomType type;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomMember> members = new ArrayList<>();

    private Integer memberCount;

    @Builder
    private ChatRoom(Long id, String name, ChatRoomType type,
                     List<ChatRoomMember> members, Integer memberCount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.members = members;
        this.memberCount = memberCount;
    }
}