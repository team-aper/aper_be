package org.aper.web.domain.review.entity;

import org.aper.web.domain.review.entity.ReviewChatRoom;
import org.aper.web.domain.global.entity.BaseSoftDeleteEntity;
import org.aper.web.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseSoftDeleteEntity {
    @Column
    private String revieweePenName;

    @Column
    private String reviewerPenName;

    @ManyToOne
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewDetail> reviewDetailList;

    @OneToOne
    @JoinColumn(name = "chat_room_id")
    private ReviewChatRoom chatRoom;

    @Builder
    public Review( String revieweePenName, String reviewerPenName, User reviewee, User reviewer, ReviewChatRoom chatRoom) {
        this.revieweePenName = revieweePenName;
        this.reviewerPenName = reviewerPenName;
        this.reviewee = reviewee;
        this.reviewer = reviewer;
        this.chatRoom = chatRoom;
        this.reviewDetailList = new ArrayList<>();
    }
}
