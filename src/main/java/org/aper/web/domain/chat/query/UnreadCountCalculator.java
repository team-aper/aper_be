package org.aper.web.domain.chat.query;

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

    /**
     * 단일 채팅방의 안읽은 메시지 수 계산 (Redis 캐싱)
     */
    public Integer calculate(Long chatRoomId, Long userId, UserReadTrackingDocument tracking, MessageDocument lastMessage) {
        // 1. Redis 캐시 확인
        Integer cachedCount = cacheService.getCachedUnreadCount(chatRoomId, userId);
        if (cachedCount != null) {
            log.debug("Cache hit - chatRoomId: {}, userId: {}, count: {}", chatRoomId, userId, cachedCount);
            return cachedCount;
        }

        // 2. 캐시 미스 - 계산
        Integer unreadCount = calculateFromDatabase(chatRoomId, tracking, lastMessage);

        // 3. Redis에 캐싱
        cacheService.cacheUnreadCount(chatRoomId, userId, unreadCount);

        return unreadCount;
    }

    /**
     * DB에서 안읽은 메시지 개수 계산
     */
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

    /**
     * Batch로 여러 채팅방의 안읽은 메시지 수 조회 (Redis 우선)
     */
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