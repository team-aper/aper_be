//package org.aper.web.domain.chat.repository;
//
//import org.aper.web.domain.chat.entity.ChatRoom;
//import org.aper.web.domain.chat.entity.Message;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
// TODO: 메시지 database -> mongodb로 이전
//@Repository
//public interface MessageRepository extends JpaRepository<Message,Long> {
//
//    // 채팅방의 최근 메시지 1개 조회
//    Optional<Message> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
//
//    // 여러 채팅방의 최근 메시지를 한번에 조회 (N+1 방지)
//    @Query("SELECT m FROM MessageEntity m WHERE m.id IN " +
//            "(SELECT MAX(m2.id) FROM MessageEntity m2 " +
//            "WHERE m2.chatRoom.id IN :chatRoomIds " +
//            "GROUP BY m2.chatRoom.id)")
//    List<Message> findLatestMessagesByChatRoomIds(@Param("chatRoomIds") List<Long> chatRoomIds);
//
//    // 특정 ID 이후의 메시지 개수 조회 (읽지 않은 메시지 수)
//    @Query("SELECT COUNT(m) FROM MessageEntity m " +
//            "WHERE m.chatRoom.id = :chatRoomId " +
//            "AND m.id > :messageId")
//    Integer countUnreadMessages(@Param("chatRoomId") Long chatRoomId,
//                                @Param("messageId") Long messageId);
//
//    // 채팅방의 전체 메시지 개수
//    @Query("SELECT COUNT(m) FROM MessageEntity m WHERE m.chatRoom.id = :chatRoomId")
//    Integer countByChatRoomId(@Param("chatRoomId") Long chatRoomId);
//
//    @Query("""
//        SELECT m FROM MessageEntity m
//        WHERE m.chatRoom IN :chatRooms
//        AND m.id IN (
//            SELECT MAX(m2.id) FROM MessageEntity m2
//            WHERE m2.chatRoom IN :chatRooms
//            GROUP BY m2.chatRoom
//        )
//    """)
//    List<Message> findLatestMessagesByChatRooms(List<ChatRoom> chatRooms);
//
//    Message findByChatRoom(ChatRoom chatRoom);
//
//    @Query("""
//        SELECT m FROM MessageEntity m
//        WHERE m.chatRoom = :chatRoom
//        ORDER BY m.createdAt DESC
//    """)
//    Message findLatestMessage(@Param("chatRoom") ChatRoom chatRoom);
//}
