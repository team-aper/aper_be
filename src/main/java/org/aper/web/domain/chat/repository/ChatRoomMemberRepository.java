package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember,Long> {
    Long countByUserUserIdAndIsTutorTrue(Long userId);
}
