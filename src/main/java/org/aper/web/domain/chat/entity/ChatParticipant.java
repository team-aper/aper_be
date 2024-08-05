package org.aper.web.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.aper.web.domain.user.entity.User;

@Entity
@Getter
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private Boolean isTutor;

    public ChatParticipant(){}

    public ChatParticipant(ChatRoom chatRoom, User user, Boolean isTutor) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.isTutor = isTutor;
    }
}
