package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember,Long> {
    Long countByUserUserIdAndIsTutorTrue(Long userId);

    List<ChatRoomMember> findByChatRoom(ChatRoom chatRoom);

    List<ChatRoomMember> findByChatRoomId(Long chatRoomId);

    Optional<ChatRoomMember> findByChatRoomAndUserUserId(ChatRoom chatRoom, Long userId);

    @Query("SELECT m.user.userId FROM ChatRoomMemberEntity m WHERE m.chatRoom = :chatRoom")
    List<Long> findMemberIdsByChatRoom(@Param("chatRoom") ChatRoom chatRoom);
}
