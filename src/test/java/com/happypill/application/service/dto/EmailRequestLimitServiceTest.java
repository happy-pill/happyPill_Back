package com.happypill.application.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
/*
1. key 값이 없으면 초기화
2. key 값이 있고 remainCnt == 1 이상이면 요청 가능
3. key 값이 있고 remainCnt == 0 이면 요청 불가능

*/
@ExtendWith(MockitoExtension.class)
class EmailRequestLimitServiceTest {

    private final String testEmail = "test@example.com";
    private final String key = "EmailRequestCnt:" + testEmail;
    @Mock
    private RedisTemplate<String, Integer> redisTemplate;
    @Mock
    private ValueOperations<String, Integer> valueOperations;
    @InjectMocks
    private EmailRequestLimitService limitService;

    @DisplayName("Redis 키 없으면 초기화하고 true 를 반환한다.")
    @Test
    void canRequestCode_1() {
        // given
        given(redisTemplate.hasKey(key)).willReturn(false);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(key)).willReturn(2);

        // when
        boolean result = limitService.canRequestCode(testEmail);

        // then
        assertThat(result).isTrue();
        //verify(redisTemplate).opsForValue();
        //verify(valueOperations).set(eq(key), eq(2), any(Duration.class));
    }

    @DisplayName("Redis 키가 있고 남은횟수 0이면 false 를 반환한다.")
    @Test
    void canRequestCode_2() {
        // given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(redisTemplate.hasKey(key)).willReturn(true);
        given(valueOperations.get(key)).willReturn(0);

        // when
        boolean result = limitService.canRequestCode(testEmail);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("Redis 키가 있고 남은횟수 1이상이면 true 를 반환한다.")
    @Test
    void canRequestCode_3() {
        // given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(redisTemplate.hasKey(key)).willReturn(true);
        given(valueOperations.get(key)).willReturn(1);

        // when
        boolean result = limitService.canRequestCode(testEmail);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("Redis 에서 예외발생시 예외를 던진다.")
    @Test
    void canRequestCode_4() {
        // given
        given(redisTemplate.hasKey(key)).willThrow(new RuntimeException("Redis 실패"));

        // when // then
        assertThatThrownBy(() -> limitService.canRequestCode(testEmail))
                .isInstanceOf(Exception.class)
                .hasMessage("Redis 실패");
    }

    @DisplayName("Redis 키가 있고 TTL 이 남아있으면 요청가능 횟수가 1회 차감된다.")
    @Test
    void decreaseRequestCnt_1() {
        // given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(redisTemplate.hasKey(key)).willReturn(true);
        given(redisTemplate.getExpire(key)).willReturn(1800L); // 30분 남음

        // when
        limitService.decreaseRequestCnt(testEmail);

        // then
        verify(valueOperations, never()).set(eq(key), eq(2), any(Duration.class));
        verify(valueOperations).decrement(key);
        verify(redisTemplate).expire(key, 1800L, TimeUnit.SECONDS);
    }
}
