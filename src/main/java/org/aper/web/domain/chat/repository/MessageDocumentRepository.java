package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.document.MessageDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MessageDocumentRepository extends MongoRepository<MessageDocument, String> {

    // 채팅방별 메시지 조회 ( 최신순, 페이징 )
    Slice<MessageDocument> findByChatRoomIdAndIsDeletedFalseOrderByCreatedAtDesc (
            Long chatRoomId,
            Pageable pageable
    );

    // 특정 시간 이후 메시지 조회 TODO: 이거 너무 긴데
    Slice<MessageDocument> findByChatRoomIdAndCreatedAtAfterAndIsDeletedFalseOrderByCreatedAtDesc(
            Long chatRoomId,
            LocalDateTime after,
            Pageable pageable
    );
}
