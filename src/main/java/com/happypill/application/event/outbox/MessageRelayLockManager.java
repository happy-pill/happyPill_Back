package com.happypill.application.event.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class MessageRelayLockManager {

    private static final String LOCK_KEY = "happypill:outbox:publish-lock";

    private final StringRedisTemplate redisTemplate;

    @Value("${message-relay.lock-duration:120s}")
    private Duration lockDuration;

    boolean getLock() {
        return Boolean.TRUE.equals(redisTemplate.opsForValue()
                .setIfAbsent(LOCK_KEY, "", lockDuration));

    }

    void releaseLock() {
        redisTemplate.delete(LOCK_KEY);
    }

}
