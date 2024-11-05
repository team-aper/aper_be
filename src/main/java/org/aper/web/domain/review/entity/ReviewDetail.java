package org.aper.web.domain.review.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.user.entity.constant.ReviewTypeEnum;

@Entity
@Getter
@Table(name = "review_detail")
@NoArgsConstructor
public class ReviewDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewTypeEnum reviewType;

    @Builder
    public ReviewDetail(ReviewTypeEnum reviewType, Review review) {
        this.reviewType = reviewType;
        this.review = review;
    }
}
