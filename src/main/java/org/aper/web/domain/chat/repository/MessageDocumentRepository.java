package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.document.MessageDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDocumentRepository extends MongoRepository<MessageDocument, String> {
    Slice<MessageDocument> findByChatRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(
            Long chatRoomId,
            Pageable pageable
    );

    Slice<MessageDocument> findByChatRoomIdAndCreatedAtAfterAndIsDeletedFalseOrderByCreatedAtDesc(
            Long chatRoomId,
            java.time.LocalDateTime after,
            Pageable pageable
    );

    MessageDocument findTopByChatRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(Long chatRoomId);

}
