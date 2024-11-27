package com.aperlibrary.chat.entity;

import com.aperlibrary.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

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

    private LocalDateTime lastVisited;

    public ChatParticipant(){}

    public ChatParticipant(ChatRoom chatRoom, User user, Boolean isTutor) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.isTutor = isTutor;
    }

    public void setLastVisited() {
        this.lastVisited = LocalDateTime.now();
    }
}