package org.aper.web.domain.review.repository;

import com.aperlibrary.review.entity.ReviewDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewDetailRepository extends JpaRepository<ReviewDetail, Long> {
    @Query("SELECT rd " +
            "FROM ReviewDetail rd " +
            "JOIN rd.review r " +
            "WHERE r.reviewee.userId = :userId")
    List<ReviewDetail> findReviewDetailsByUserId(Long userId);
}
