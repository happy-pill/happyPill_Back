package com.happypill.application.service.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailRequestLimitService { //회원마다 인증요청 횟수를 처리하는 로직

    private static final int MAX_REQUESTS = 3;
    private static final Duration TTL = Duration.ofHours(24);

    private final RedisTemplate<String, Integer> redisTemplateInteger;

    private String getKey(String loginEmail) {
        return "EmailRequestCnt:" + loginEmail;
    }

    private void initializeKeyIfNotExists(String key){ // 키 값이 없는 경우 요청횟수를 초기화하는 메서드
        Boolean hasKey = redisTemplateInteger.hasKey(key);
        if(!hasKey)
            redisTemplateInteger.opsForValue().set(key, MAX_REQUESTS, TTL);
    }

    public boolean canRequestCode(String loginEmail) {  // 요청 가능한지 확인하는 메서드
        try {
            String key = getKey(loginEmail);

            initializeKeyIfNotExists(key);

            //남은 요청 횟수를 remainCnt 에 저장
            Integer remainCnt = redisTemplateInteger.opsForValue().get(key);

            return remainCnt != 0;
        } catch (Exception e) {
            log.error("Redis 요청 가능한지 확인 실패 : {}", e.getMessage());
            throw e;
        }
    }

    public void decreaseRequestCnt(String loginEmail) { //인증요청 성공 시 차감하는 메서드
        try {
            String key = getKey(loginEmail);

            initializeKeyIfNotExists(key);

            //남은 TTL 저장
            Long expire = redisTemplateInteger.getExpire(key);

            //요청횟수 감소
            redisTemplateInteger.opsForValue().decrement(key);

            // TTL 재설정(decrement 가 TTL 를 변경하거나 제거할 수 있기 때문에)
            if (expire != null && expire > 0) {
                redisTemplateInteger.expire(key, expire, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("Redis 요청횟수 차감 실패 : {}", e.getMessage());
            throw e;
        }
    }
}