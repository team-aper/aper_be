package org.aper.web.global.jwt.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    public RefreshTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public void saveTokenInfo(String email, String refreshToken, long expiredTime) {
        String refreshTokenKey = "refreshToken:" + email;
        redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, expiredTime, TimeUnit.MILLISECONDS);
    }

    @Transactional
    public String getRefreshToken(String email) {
        return String.valueOf(redisTemplate.opsForValue().get("refreshToken:" + email));
    }

    @Transactional
    public void deleteRefreshToken(String email) {
        String refreshTokenKey = "refreshToken:" + email;
        redisTemplate.delete(refreshTokenKey);
    }
}
