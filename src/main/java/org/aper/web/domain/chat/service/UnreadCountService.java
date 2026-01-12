package org.aper.web.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.aper.web.domain.chat.document.UserReadTrackingDocument;
import org.aper.web.domain.chat.query.UnreadCountCalculator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnreadCountService {

    private final UnreadCountCacheService cache;
    private final UnreadCountCalculator calculator;

    public Map<Long, Integer> resolveUnreadCounts(Long userId,
                                                  List<Long> roomIds,
                                                  Map<Long, ChatRoomSummaryDocument> summaryMap,
                                                  Map<Long, UserReadTrackingDocument> trackingMap) {
        Map<Long, Integer> cached = cache.getBulkUnreadCounts(userId, roomIds);
        Map<Long, Integer> result = new HashMap<>(cached);

        for (Long roomId : roomIds) {
            if (result.containsKey(roomId)) continue;

            int unread = calculator.calculate(roomId, summaryMap.get(roomId), trackingMap.get(roomId));
            result.put(roomId, unread);

            cache.cacheUnreadCount(roomId, userId, unread);
        }
        return result;
    }

}
