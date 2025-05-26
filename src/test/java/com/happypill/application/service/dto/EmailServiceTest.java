package com.happypill.application.service.dto;

import com.happypill.application.exception.EmailException;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private static final long CODE_EXPIRATION_TIME = 180;

    private final String loginEmail = "test@gmail.com";
    private final String newEmail = "test3@gmail.com";
    private final String code = "123456";
    private final String key = "NewEmailRequest:" + newEmail;

    @Mock
    private HappypillUserRepository userRepository;

    @Mock
    private EmailRequestLimitService emailRequestLimitService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @DisplayName("인증코드 생성 시 6자리 숫자로만 이루어진다.")
    @Test
    void generateCode_1() {
        //when
        Mockito.reset(redisTemplate, valueOperations);
        String code = emailService.generateCode();

        //then
        assertThat(code)
                .hasSize(6)
                .matches("\\d{6}");
    }

    @DisplayName("조건문을 모두 통과했을 때 [이메일 전송]과 [Redis 유효시간 저장 로직]은 정상적으로 작동한다.")
    @Test
    void sendEmail_1() {
        //given
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        given(javaMailSender.createMimeMessage()).willReturn(mimeMessage);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(emailRequestLimitService.canRequestCode(anyString())).willReturn(true);

        //when
        emailService.sendEmail(loginEmail, newEmail);

        //then
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
        verify(valueOperations).set(
                argThat(key -> key.contains(newEmail)),
                anyString(),
                eq(CODE_EXPIRATION_TIME),
                eq(TimeUnit.SECONDS)
        );
    }

    @DisplayName("수정하려는 이메일이 이미 존재하는 이메일일 때 예외가 발생한다.")
    @Test
    void sendEmail_2() {
        //given
        given(userRepository.existsByNotifyEmail(newEmail)).willReturn(true);

        //when //then
        assertThatThrownBy(() -> emailService.sendEmail(loginEmail, newEmail))
                .isInstanceOf(EmailException.class)
                .hasMessage("이미 사용 중인 이메일입니다.");
    }

    @DisplayName("요청 횟수를 초과했을 경우 예외를 던진다.")
    @Test
    void sendEmail_3() {
        //given
        given(emailRequestLimitService.canRequestCode(loginEmail)).willReturn(false);

        //when //then
        assertThatThrownBy(() -> emailService.sendEmail(loginEmail, newEmail))
                .isInstanceOf(EmailException.class)
                .hasMessage("인증요청 횟수를 초과하였습니다.");
    }


    @DisplayName("인증코드가 일치하면 true 를 반환한다.")
    @Test
    void confirmCode_1() {
        //given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(key)).willReturn(code);

        //when //then
        assertThatCode(() -> emailService.confirmCode(newEmail, code))
                .doesNotThrowAnyException();
        verify(redisTemplate).delete(key);
    }

    @DisplayName("인증 코드가 불일치하면 false 를 반환한다.")
    @Test
    void confirmCode_2() {
        //given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(key)).willReturn("333333");

        //when //then
        assertThatThrownBy(() -> emailService.confirmCode(newEmail, code))
                .isInstanceOf(EmailException.class)
                .hasMessage("인증코드가 일치하지 않거나 만료되었습니다.");
        verify(redisTemplate, never()).delete(key);
    }
}