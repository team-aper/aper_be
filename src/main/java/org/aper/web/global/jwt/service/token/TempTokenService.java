package org.aper.web.global.jwt.service.token;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TempTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public String generateTempToken(String email) {
        String tempToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(tempToken, email, 5, TimeUnit.MINUTES);
        return tempToken;
    }

    public String getEmailByTempToken(String tempToken) {
        return redisTemplate.opsForValue().get(tempToken);
    }

    public void deleteTempToken(String tempToken) {
        redisTemplate.delete(tempToken);
    }
}
