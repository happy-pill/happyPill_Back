package com.happypill.api.config.auth.jwt;

import com.happypill.application.util.DataSerializerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RefreshSessionRepository {
    private static final String KEY_PATTERN = "refresh:{%d}:%s";
    private final StringRedisTemplate redisTemplate;

    private String generateKey(long userId, String uuid) {
        return String.format(KEY_PATTERN, userId, uuid);
    }

    public Optional<String[]> getUserInfo(long userId, String secret) {
        String raw = redisTemplate.opsForValue()
                .get(generateKey(userId, secret));
        try {
            return Optional.of(DataSerializerUtil.deserialize(raw, String[].class));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }


    public void save(long userId, String secret, String[] roles, Duration ttl) {
        String raw = DataSerializerUtil.serialize(roles);
        redisTemplate.opsForValue()
                .set(generateKey(userId, secret), raw, ttl);
    }

    /**
     * 로그아웃시 사용
     */
    public boolean delete(long userId, String secret) {
        return redisTemplate.delete(generateKey(userId, secret));
    }

    /**
     * 로테이션시 사용
     */
    public Optional<String[]> getUserInfoAndDelete(long userId, String secret) {
        String raw = redisTemplate.opsForValue()
                .getAndDelete(generateKey(userId, secret));
        try {
            return Optional.of(DataSerializerUtil.deserialize(raw, String[].class));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

//    해킹당했을때
//    public void revokeAll(){
//
//    }

}
