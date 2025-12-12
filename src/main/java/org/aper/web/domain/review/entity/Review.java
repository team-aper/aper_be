package org.aper.web.domain.review.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.common.entity.BaseSoftDeleteEntity;
import org.aper.web.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor
public class Review extends BaseSoftDeleteEntity {

    @Column(name = "reviewee_pen_name")
    private String revieweePenName;

    @Column(name = "reviewer_pen_name")
    private String reviewerPenName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewDetail> reviewDetailList = new ArrayList<>();

    // ChatRoom 참조 제거 (aper_chat_renewal 소속)
    // 필요시 chatRoomId만 저장
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @Builder
    public Review(String revieweePenName, String reviewerPenName, User reviewee, User reviewer, Long chatRoomId) {
        this.revieweePenName = revieweePenName;
        this.reviewerPenName = reviewerPenName;
        this.reviewee = reviewee;
        this.reviewer = reviewer;
        this.chatRoomId = chatRoomId;
        this.reviewDetailList = new ArrayList<>();
    }
}
