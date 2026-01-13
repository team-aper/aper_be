package org.aper.web.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class UnreadCountCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String UNREAD_COUNT_PREFIX = "chat:unread:";
    private static final long CACHE_TTL_HOURS = 24;


    public void cacheUnreadCount(Long chatRoomId, Long userId, Integer count) {
        String key = getKey(chatRoomId, userId);
        redisTemplate.opsForValue().set(key, String.valueOf(count), CACHE_TTL_HOURS, TimeUnit.HOURS);
        log.debug("Cached unread count - chatRoomId: {}, userId: {}, count: {}", chatRoomId, userId, count);
    }

    public Integer getCachedUnreadCount(Long chatRoomId, Long userId) {
        String key = getKey(chatRoomId, userId);
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null; // 캐시 미스
        }

        return Integer.parseInt(value);
    }

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

    public void incrementUnreadCount(Long chatRoomId, Long userId) {
        String key = getKey(chatRoomId, userId);
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, CACHE_TTL_HOURS, TimeUnit.HOURS);
    }


    public void resetUnreadCount(Long chatRoomId, Long userId) {
        String key = getKey(chatRoomId, userId);
        redisTemplate.opsForValue().set(key, "0", CACHE_TTL_HOURS, TimeUnit.HOURS);
        log.debug("Reset unread count - chatRoomId: {}, userId: {}", chatRoomId, userId);
    }

    public void incrementUnreadCountForMembers(Long chatRoomId, List<Long> memberIds, Long senderId) {
        for (Long memberId : memberIds) {
            if (!memberId.equals(senderId)) {
                incrementUnreadCount(chatRoomId, memberId);
            }
        }
    }

    public void evictCache(Long chatRoomId, Long userId) {
        String key = getKey(chatRoomId, userId);
        redisTemplate.delete(key);
    }

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

    public Map<Long, Integer> getBulkUnreadCounts(Long userId, List<Long> chatRoomIds) {
        if (chatRoomIds == null || chatRoomIds.isEmpty()) {
            return new HashMap<>();
        }

        List<String> keys = new ArrayList<>();
        for(Long chatRoomId : chatRoomIds) {
            keys.add(getKey(chatRoomId, userId));
        }

        List<String> values = redisTemplate.opsForValue().multiGet(keys);

        Map<Long, Integer> result = new HashMap<>();
        for (int i = 0; i < chatRoomIds.size(); i++) {
            String value = values != null ? values.get(i) : null;
            if (value == null) continue;

            try {
                result.put(chatRoomIds.get(i), Integer.parseInt(value));
            } catch (NumberFormatException e) {
                log.warn("Invalid cached count for chatRoomId: {}, value: {}", chatRoomIds.get(i), value);
            }
        }

        log.debug("Bulk fetched unread counts - userId: {}, roomCount: {}, hitCount: {}",
                userId, chatRoomIds.size(), result.size());

        return result;
    }
}