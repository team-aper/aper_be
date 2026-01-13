package org.aper.web.domain.chat.query;

import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.aper.web.domain.chat.document.MessageDocument;
import org.aper.web.domain.chat.document.UserReadTrackingDocument;
import org.aper.web.domain.chat.repository.MessageDocumentRepository;
import org.aper.web.domain.chat.service.UnreadCountCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UnreadCountCalculator {

    private final MessageDocumentRepository messageDocumentRepository;
    private final UnreadCountCacheService cacheService;

    public Integer calculate(Long roomId, ChatRoomSummaryDocument summary, UserReadTrackingDocument tracking) {
        if (summary == null || summary.getCurrentSequence() == null) return 0;

        long lastSeq = summary.getCurrentSequence();
        long readSeq = (tracking != null && tracking.getLastReadSequence() != null)
                ? tracking.getLastReadSequence()
                : 0L;

        return (int) Math.max(0, lastSeq - readSeq);
    }

    private Integer calculateFromDatabase(Long chatRoomId, UserReadTrackingDocument tracking, MessageDocument lastMessage) {
        if (lastMessage == null) {
            return 0;
        }

        if (tracking == null || tracking.getLastReadSequence() == null) {
            // 한 번도 읽지 않은 경우: 전체 메시지 개수
            return messageDocumentRepository.countByChatRoomIdAndIsDeletedFalse(chatRoomId).intValue();
        }

        // 마지막 읽은 시퀀스 이후의 메시지 개수
        return messageDocumentRepository
                .countUnreadMessages(chatRoomId, tracking.getLastReadSequence())
                .intValue();
    }

    public Map<Long, Integer> calculateBatch(
            Long userId,
            List<Long> chatRoomIds,
            Map<Long, UserReadTrackingDocument> trackingMap,
            Map<Long, MessageDocument> latestMessageMap
    ) {
        Map<Long, Integer> result = new HashMap<>();

        // 1. Redis에서 일괄 조회
        Map<Long, Integer> cachedCounts = cacheService.getCachedUnreadCounts(userId, chatRoomIds);

        for (Long chatRoomId : chatRoomIds) {
            // 2. 캐시에 있으면 사용
            if (cachedCounts.containsKey(chatRoomId)) {
                result.put(chatRoomId, cachedCounts.get(chatRoomId));
                continue;
            }

            // 3. 캐시 미스 - 계산 후 캐싱
            UserReadTrackingDocument tracking = trackingMap.get(chatRoomId);
            MessageDocument lastMessage = latestMessageMap.get(chatRoomId);

            Integer unreadCount = calculateFromDatabase(chatRoomId, tracking, lastMessage);
            cacheService.cacheUnreadCount(chatRoomId, userId, unreadCount);

            result.put(chatRoomId, unreadCount);
        }

        return result;
    }
}