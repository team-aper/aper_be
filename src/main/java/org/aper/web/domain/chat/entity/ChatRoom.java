package org.aper.web.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private Boolean isAccepted;

    private Boolean isRejected;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    public ChatRoom(){
        this.startTime = LocalDateTime.now();
        this.isAccepted = false;
        this.isRejected = false;
    }

    public void reject() {
        this.isRejected = true;
    }
}
