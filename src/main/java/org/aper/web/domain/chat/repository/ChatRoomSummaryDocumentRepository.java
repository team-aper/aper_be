package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomSummaryDocumentRepository extends MongoRepository<ChatRoomSummaryDocument, String> {

    Optional<ChatRoomSummaryDocument> findByChatRoomId(Long chatRoomId);

    List<ChatRoomSummaryDocument> findByChatRoomIdIn(List<Long> chatRoomIds);
}
