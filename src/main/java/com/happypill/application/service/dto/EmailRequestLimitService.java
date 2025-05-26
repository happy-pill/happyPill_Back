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

    private static final int MAX_REQUESTS = 2;
    private static final Duration TTL = Duration.ofHours(24);

    private final RedisTemplate<String, Integer> redisTemplateInteger;

    private String getKey(String loginEmail) {
        return "EmailRequestCnt:" + loginEmail;
    }

    public boolean canRequestCode(String loginEmail) {  // 요청 가능한지 확인하는 메서드. 키가 없을 때 요청횟수를 2로 초기화
        String key = getKey(loginEmail);
        try {
            Boolean hasKey = redisTemplateInteger.hasKey(key);

            //해당 키값이 없으면 요청횟수 2로 초기화(TTL 이 만료된 경우)
            if (!hasKey) {
                redisTemplateInteger.opsForValue().set(key, MAX_REQUESTS, TTL);
                System.out.println("["+ key + "] Redis 에 요청횟수 초기화 성공");
            }
            //남은 TTL 저장
            Long expire = redisTemplateInteger.getExpire(key);

            //남은 요청 횟수를 remainCnt 에 저장
            Integer remainCnt = redisTemplateInteger.opsForValue().get(key);
            System.out.println("["+ key + "] 의 요청횟수 : " + remainCnt);
            System.out.println("["+ key + "] 의 잔여시간 : " + expire);
            return remainCnt != 0;
        } catch (Exception e) {
            log.error("Redis 요청 가능한지 확인 실패 : {}", e.getMessage());
            throw e;
        }
    }

    public void decreaseRequestCnt(String loginEmail) { //인증요청 성공 시 차감하는 메서드
        String key = getKey(loginEmail);
        try {
            Boolean hasKey = redisTemplateInteger.hasKey(key);

            //해당 키값이 없으면 요청횟수 2로 초기화(TTL 이 만료된 경우)
            if (!hasKey) {
                redisTemplateInteger.opsForValue().set(key, MAX_REQUESTS, TTL);
            }
            //남은 TTL 저장
            Long expire = redisTemplateInteger.getExpire(key);

            //요청횟수 감소
            redisTemplateInteger.opsForValue().decrement(key);
            Integer remainCnt = redisTemplateInteger.opsForValue().get(key);
            System.out.println("[" + key+ "] 의 요청횟수 감소!!!");
            System.out.println("[" + key+ "] 의 요청가능 횟수 : " + remainCnt);
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
/*
사용자가 이메일을 요청하는 순간 redis 의 key 값에 사용자의 loginEmail 정보가 없는 경우
-> 사용자의 loginEmail 을 기준으로 [요청횟수]는 2로 초기화

사용자가 이메일을 요청하는 순간 redis 의 key 값에 사용자의 loginEmail 정보가 있는 경우
-> 사용자의 loginEmail 을 기준으로 [요청횟수]는 -1 감소

사용자가 인증요청 버튼을 누르고 응답이 OK 인 순간 [요청횟수] 는 1 차감
*/
