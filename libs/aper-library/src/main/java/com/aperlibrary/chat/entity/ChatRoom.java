package com.aperlibrary.chat.entity;

import com.aperlibrary.review.entity.Review;
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

    private Boolean isRequested;

    private Boolean isAccepted;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id")
    private Review review;

    public ChatRoom() {
        this.startTime = LocalDateTime.now();
        this.isAccepted = Boolean.FALSE;
        this.isRequested = Boolean.FALSE;
    }

    public void reject() {
        this.isAccepted = Boolean.FALSE;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
        this.isRequested = Boolean.FALSE;
    }

    public void setTerminate(Boolean terminate) {
        this.isAccepted = terminate;
        this.isRequested = Boolean.FALSE;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public void setIsRequested(Boolean aTrue) {
        this.isRequested = aTrue;
    }

}
