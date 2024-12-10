package org.aper.web.domain.user.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class EmailRepository {

    private final StringRedisTemplate redisTemplate;

    public EmailRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /* 이메일 인증 유효기간 10분 */
    public void saveCertificationNumber(String email, String certificationNumber) {
        redisTemplate.opsForValue()
            .set(email, certificationNumber, Duration.ofMinutes(10));
    }

    public String getCertificationNumber(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void removeCertificationNumber(String email) {
        redisTemplate.delete(email);
    }

    public boolean hashKey(String email) {
        Boolean keyExists = redisTemplate.hasKey(email);
        return keyExists != null & Boolean.TRUE.equals(keyExists);
    }
}
