package org.aper.web.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.aper.web.domain.user.entity.User;

@Entity
@Getter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Chat(){}


}
