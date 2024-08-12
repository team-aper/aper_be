package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findByChatRoomId(Long chatRoomId);

    List<ChatParticipant> findByUserUserId(Long userId);

    List<ChatParticipant> findByIsTutorAndUserUserId(boolean b, Long tutorId);

    Optional<ChatParticipant> findByIsTutorAndUserUserIdAndChatRoomId(boolean b, Long tutorId, Long roomId);
}
