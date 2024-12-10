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

    // 임시 토큰 생성 및 저장
    public String generateTempToken(String email) {
        String tempToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(tempToken, email, 5, TimeUnit.MINUTES); // 5분 TTL 설정
        return tempToken;
    }

    // 임시 토큰으로 이메일 조회
    public String getEmailByTempToken(String tempToken) {
        return redisTemplate.opsForValue().get(tempToken);
    }

    // 임시 토큰 삭제
    public void deleteTempToken(String tempToken) {
        redisTemplate.delete(tempToken);
    }
}
