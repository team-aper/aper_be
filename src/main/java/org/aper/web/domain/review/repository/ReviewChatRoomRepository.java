package org.aper.web.domain.review.repository;

import org.aper.web.domain.review.entity.ReviewChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewChatRoomRepository extends JpaRepository<ReviewChatRoom, Long> {

    @Query("SELECT c FROM ReviewChatRoom c WHERE c.id = :chatRoomId")
    Optional<ReviewChatRoom> findByIdForReview(@Param("chatRoomId") Long chatRoomId);
}
