package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.document.UserReadTrackingDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReadTrackingDocumentRepository extends MongoRepository<UserReadTrackingDocument, String> {

    Optional<UserReadTrackingDocument> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    List<UserReadTrackingDocument> findByUserIdAndChatRoomIdIn(Long userId, List<Long> chatRoomIds);

    List<UserReadTrackingDocument> findByChatRoomId(Long chatRoomId);
}