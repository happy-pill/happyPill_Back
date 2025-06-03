package com.happypill.application.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EmailVerificationRedisRepository {

    // email:verification:request-count:1
    private static final String EMAIL_VERIFICATION_REQUEST_COUNT = "email:verification:request-count:%s";

    // email:verification:verifyCode:11111:22222
    private static final String EMAIL_VERIFICATION_CODE_KEY = "email:verification:verifyCode:%s:%s";

    private final StringRedisTemplate redisTemplate;

    private String generateRequestCountKey(String userId) {
        return String.format(EMAIL_VERIFICATION_REQUEST_COUNT, userId);
    }

    private String generateVerificationCodeKey(String userId, String notifyEmail) {
        return String.format(EMAIL_VERIFICATION_CODE_KEY, userId, notifyEmail);
    }

    public Long increaseRequestCount(Long userId, Duration ttl) { //TODO lua script
        String key = generateRequestCountKey(String.valueOf(userId));
        log.info("key = {}", key);
        if (redisTemplate.opsForValue().setIfAbsent(key, "1", ttl)) {
            return 1L;
        }
        return redisTemplate.opsForValue().increment(key);
    }

    public void saveVerificationCode(Long userId, String notifyEmail, String verifyCode, Duration ttl) {
        String key = generateVerificationCodeKey(String.valueOf(userId), notifyEmail);
        redisTemplate.opsForValue().set(key, verifyCode, ttl);
    }

    public Optional<String> getVerificationCode(Long userId, String notifyEmail) {
        String key = generateVerificationCodeKey(String.valueOf(userId), notifyEmail);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

}
