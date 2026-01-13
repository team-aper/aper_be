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
    Slice<MessageDocument> findByChatRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(
            Long chatRoomId,
            Pageable pageable
    );


    Slice<MessageDocument> findByChatRoomIdAndCreatedAtAfterAndIsDeletedFalseOrderByCreatedAtDesc(
            Long chatRoomId,
            LocalDateTime after,
            Pageable pageable
    );


    Optional<MessageDocument> findTopByChatRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(Long chatRoomId);


    @Query(value = "{ 'chatRoomId': { $in: ?0 }, 'isDeleted': false }",
            sort = "{ 'createdAt': -1 }")
    List<MessageDocument> findLatestMessagesByChatRoomIds(List<Long> chatRoomIds);


    @Query(value = "{ 'chatRoomId': ?0, 'messageSequence': { $gt: ?1 }, 'isDeleted': false }",
            count = true)
    Long countUnreadMessages(Long chatRoomId, Long lastReadSequence);


    Long countByChatRoomIdAndIsDeletedFalse(Long chatRoomId);
}
