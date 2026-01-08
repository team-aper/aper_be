package org.aper.web.domain.review.repository;

import org.aper.web.entity.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c WHERE c.id = :chatRoomId")
    Optional<ChatRoom> findByIdForReview(@Param("chatRoomId") Long chatRoomId);
}
