package com.aperlibrary.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "chat_room_participants_view")
@Getter
public class ChatRoomView {

    @Id
    private Long chatRoomId;
    private String participants;
    public ChatRoomView() {

    }
}