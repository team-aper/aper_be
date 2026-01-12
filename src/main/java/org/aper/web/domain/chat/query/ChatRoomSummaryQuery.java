package org.aper.web.domain.chat.query;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.aper.web.domain.chat.repository.ChatRoomSummaryDocumentRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatRoomSummaryQuery {
    private final ChatRoomSummaryDocumentRepository repository;

    public Map<Long, ChatRoomSummaryDocument> getSummaryMap(List<Long> roomIds) {
        List<ChatRoomSummaryDocument> documents = repository.findByChatRoomIdIn(roomIds);
        Map<Long, ChatRoomSummaryDocument> resultMap = new HashMap<>();

        for(ChatRoomSummaryDocument document : documents) {
            Long roomId = document.getChatRoomId();
            if (!resultMap.containsKey(roomId)) {
                resultMap.put(roomId, document);
            }
        }
        return resultMap;
    }
}
