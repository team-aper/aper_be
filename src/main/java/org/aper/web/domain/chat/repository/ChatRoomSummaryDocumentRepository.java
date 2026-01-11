package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomSummaryDocumentRepository extends MongoRepository<ChatRoomSummaryDocument, String> {

    /**
     * MySQL chatRoomId로 조회
     */
    Optional<ChatRoomSummaryDocument> findByChatRoomId(Long chatRoomId);

    /**
     * 여러 채팅방 요약 정보 조회 (Batch)
     */
    List<ChatRoomSummaryDocument> findByChatRoomIdIn(List<Long> chatRoomIds);
}
