package org.aper.web.global.jwt.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveBlackListToken(String token) {
        redisTemplate.opsForValue().set("blacklist:"+token,"true", Duration.ofDays(3));
    }

    public boolean isTokenBlacklisted(String token) {
        String isBlacklisted = redisTemplate.opsForValue().get("blacklist:" + token);
        return "true".equals(isBlacklisted);
    }
}
