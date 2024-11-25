package org.aper.web.domain.chat.repository;

import com.aperlibrary.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    /*
    todo: 종료된 수업에 한해서 find 하도록 수정
     */
    @Query("SELECT c FROM ChatRoom c WHERE c.id = :chatRoomId AND c.isAccepted = true")
    Optional<ChatRoom> findByIdForReview(Long chatRoomId);
}
