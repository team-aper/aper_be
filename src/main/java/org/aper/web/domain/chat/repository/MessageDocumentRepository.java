package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.document.MessageDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageDocumentRepository extends MongoRepository<MessageDocument, String> {
    /**
     * 채팅방별 메시지 조회 (최신순, 페이징)
     */
    Slice<MessageDocument> findByChatRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(
            Long chatRoomId,
            Pageable pageable
    );

    /**
     * 특정 시간 이후 메시지 조회
     */
    Slice<MessageDocument> findByChatRoomIdAndCreatedAtAfterAndIsDeletedFalseOrderByCreatedAtDesc(
            Long chatRoomId,
            LocalDateTime after,
            Pageable pageable
    );

    /**
     * 채팅방의 최신 메시지 1개 조회
     */
    Optional<MessageDocument> findTopByChatRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(Long chatRoomId);

    /**
     * 여러 채팅방의 최신 메시지 조회 (Batch)
     */
    @Query(value = "{ 'chatRoomId': { $in: ?0 }, 'isDeleted': false }",
            sort = "{ 'createdAt': -1 }")
    List<MessageDocument> findLatestMessagesByChatRoomIds(List<Long> chatRoomIds);

    /**
     * 특정 시퀀스 이후 메시지 개수
     */
    @Query(value = "{ 'chatRoomId': ?0, 'messageSequence': { $gt: ?1 }, 'isDeleted': false }",
            count = true)
    Long countUnreadMessages(Long chatRoomId, Long lastReadSequence);

    /**
     * 채팅방의 전체 메시지 개수
     */
    Long countByChatRoomIdAndIsDeletedFalse(Long chatRoomId);
}
