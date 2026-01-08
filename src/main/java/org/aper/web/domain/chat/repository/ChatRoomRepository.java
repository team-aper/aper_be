package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    @Query("SELECT DISTINCT cr FROM ChatRoomEntity cr " +
           "JOIN cr.members m " +
           "WHERE m.user.userId = :userId")
    List<ChatRoom> findAllByMemberUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE ChatRoomEntity cr SET cr.lastMessageAt = :messageTime WHERE cr.id = :chatRoomId")
    void updateLastMessageAt(@Param("chatRoomId") Long chatRoomId,
                            @Param("messageTime") LocalDateTime messageTime);


    @Query("""
    SELECT DISTINCT cr FROM ChatRoomEntity cr
    JOIN cr.members m
    WHERE m.user.userId = :userId
    ORDER BY cr.updatedAt DESC
    """)
    List<ChatRoom> findRecentChatRooms(@Param("userId") Long userId);


    @Query("""
    SELECT DISTINCT cr FROM ChatRoomEntity cr
    WHERE EXISTS (
        SELECT 1 FROM ChatRoomMemberEntity m
        WHERE m.chatRoom = cr AND m.user.userId = :userId
        )
    """)
    Slice<ChatRoom> findRecentChatRooms(@Param("userId") Long userId, Pageable pageable);

    Optional<ChatRoom> getChatRoomById(Long chatRoomId);
}
