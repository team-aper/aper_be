package org.aper.web.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis를 사용한 안읽은 메시지 개수 캐싱
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UnreadCountCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String UNREAD_COUNT_PREFIX = "chat:unread:";
    private static final long CACHE_TTL_HOURS = 24;

    /**
     * 안읽은 개수 캐싱 - Key: chat:unread:{chatRoomId}:{userId}
     */
    public void cacheUnreadCount(Long chatRoomId, Long userId, Integer count) {
        String key = getKey(chatRoomId, userId);
        redisTemplate.opsForValue().set(key, String.valueOf(count), CACHE_TTL_HOURS, TimeUnit.HOURS);
        log.debug("Cached unread count - chatRoomId: {}, userId: {}, count: {}", chatRoomId, userId, count);
    }

    /**
     * 캐시된 안읽은 개수 조회
     */
    public Integer getCachedUnreadCount(Long chatRoomId, Long userId) {
        String key = getKey(chatRoomId, userId);
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null; // 캐시 미스
        }

        return Integer.parseInt(value);
    }

    /**
     * 여러 채팅방의 안읽은 개수 일괄 조회
     */
    public Map<Long, Integer> getCachedUnreadCounts(Long userId, List<Long> chatRoomIds) {
        Map<Long, Integer> result = new HashMap<>();

        for (Long chatRoomId : chatRoomIds) {
            Integer count = getCachedUnreadCount(chatRoomId, userId);
            if (count != null) {
                result.put(chatRoomId, count);
            }
        }

        return result;
    }

    /**
     * 안읽은 개수 증가 (메시지 전송 시)
     */
    public void incrementUnreadCount(Long chatRoomId, Long userId) {
        String key = getKey(chatRoomId, userId);
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, CACHE_TTL_HOURS, TimeUnit.HOURS);
    }

    /**
     * 안읽은 개수 초기화 (읽음 처리 시)
     */
    public void resetUnreadCount(Long chatRoomId, Long userId) {
        String key = getKey(chatRoomId, userId);
        redisTemplate.opsForValue().set(key, "0", CACHE_TTL_HOURS, TimeUnit.HOURS);
        log.debug("Reset unread count - chatRoomId: {}, userId: {}", chatRoomId, userId);
    }

    /**
     * 채팅방의 모든 멤버에 대한 안읽은 개수 증가
     */
    public void incrementUnreadCountForMembers(Long chatRoomId, List<Long> memberIds, Long senderId) {
        for (Long memberId : memberIds) {
            if (!memberId.equals(senderId)) { // 발신자는 제외
                incrementUnreadCount(chatRoomId, memberId);
            }
        }
    }

    /**
     * 캐시 삭제
     */
    public void evictCache(Long chatRoomId, Long userId) {
        String key = getKey(chatRoomId, userId);
        redisTemplate.delete(key);
    }

    /**
     * 채팅방 전체 캐시 삭제
     */
    public void evictChatRoomCache(Long chatRoomId) {
        String pattern = UNREAD_COUNT_PREFIX + chatRoomId + ":*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    private String getKey(Long chatRoomId, Long userId) {
        return UNREAD_COUNT_PREFIX + chatRoomId + ":" + userId;
    }
}