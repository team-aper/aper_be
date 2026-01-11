package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.document.UserReadTrackingDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReadTrackingDocumentRepository extends MongoRepository<UserReadTrackingDocument, String> {

    /**
     * 사용자의 특정 채팅방 읽기 추적 조회
     */
    Optional<UserReadTrackingDocument> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    /**
     * 사용자의 여러 채팅방 읽기 추적 조회 (Batch)
     */
    List<UserReadTrackingDocument> findByUserIdAndChatRoomIdIn(Long userId, List<Long> chatRoomIds);

    /**
     * 채팅방의 모든 읽기 추적 조회
     */
    List<UserReadTrackingDocument> findByChatRoomId(Long chatRoomId);
}