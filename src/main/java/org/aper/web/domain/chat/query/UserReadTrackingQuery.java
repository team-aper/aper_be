package org.aper.web.domain.chat.query;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.chat.document.UserReadTrackingDocument;
import org.aper.web.domain.chat.repository.UserReadTrackingDocumentRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserReadTrackingQuery {
    private final UserReadTrackingDocumentRepository repository;

    public Map<Long, UserReadTrackingDocument> getTrackingMap(Long userId, List<Long> roomIds) {
        List<UserReadTrackingDocument> documents = repository.findByUserIdAndChatRoomIdIn(userId, roomIds);
        Map<Long, UserReadTrackingDocument> resultMap = new HashMap<>();

        for (UserReadTrackingDocument document : documents) {
            Long roomId = document.getChatRoomId();
            if (!resultMap.containsKey(roomId)) {
                resultMap.put(roomId, document);
            }
        }
        return resultMap;
    }
}
