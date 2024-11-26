package org.aper.web.domain.chat.repository;

import com.aperlibrary.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    Long countByUserUserIdAndIsTutorTrue(Long userId);
}
