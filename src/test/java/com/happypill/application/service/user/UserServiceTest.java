package com.happypill.application.service.user;

import com.happypill.api.config.auth.usercontext.SecurityUserContext;
import com.happypill.application.auth.UserContext;
import com.happypill.application.entity.HappypillUser;
import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.service.IntegrationTestSupport;
import com.happypill.application.service.user.request.EmailVerificationRequest;
import com.happypill.application.service.user.request.UserNotifyEmailUpdateRequest;
import com.happypill.application.util.SnowflakeUtil;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserService service;

    @Autowired
    private HappypillUserRepository userRepository;

    @Autowired
    private EmailVerificationRedisRepository emailVerificationRedisRepository;

    private final Faker faker = new Faker();

    private HappypillUser generateTestUser() {
        String email = faker.internet().emailAddress();
        return userRepository.save(HappypillUser.ofSocial(
                SnowflakeUtil.nextId(),
                "nickname",
                Provider.GOOGLE,
                String.valueOf(SnowflakeUtil.nextId()),
                email,
                email,
                Role.USER
        ));
    }

    @DisplayName("동일 유저가 4개째의 이메일 전송 요청을 보내는 순간부터 오류를 발생시킨다")
    @Test
    void test1() throws Exception {
        HappypillUser user = generateTestUser();
        UserContext userContext = SecurityUserContext.from(user.getUserId());
        EmailVerificationRequest request = new EmailVerificationRequest(faker.internet().emailAddress());

        service.sendEmailVerificationCode(userContext, request);
        service.sendEmailVerificationCode(userContext, request);
        service.sendEmailVerificationCode(userContext, request);

        assertThatThrownBy(() -> service.sendEmailVerificationCode(userContext, request))
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        e -> assertThat(e.getExceptionCode()).isEqualTo(ExceptionCode.EMAIL_REQUEST_COUNT_EXCEED)
                );
    }

    @DisplayName("이메일 검증에 성공하면 유저의 알림받을 이메일이 변경된다.")
    @Test
    void test2() throws Exception {
        HappypillUser user = generateTestUser();
        UserContext userContext = SecurityUserContext.from(user.getUserId());
        String verifyCode = faker.number().digits(6);
        UserNotifyEmailUpdateRequest request = new UserNotifyEmailUpdateRequest(verifyCode, faker.internet().emailAddress());
        emailVerificationRedisRepository.saveVerificationCode(userContext.getId(), request.notifyEmail(), verifyCode, Duration.ofMinutes(1L));

        service.verifyAndUpdateUserNotifyEmail(userContext, request);

        assertThat(user.getNotifyEmail()).isEqualTo(request.notifyEmail());
    }

    @DisplayName("이메일 검증에 실패하면 오류가 발생한다")
    @Test
    void test3() throws Exception {

        HappypillUser user = generateTestUser();
        UserContext userContext = SecurityUserContext.from(user.getUserId());
        UserNotifyEmailUpdateRequest request = new UserNotifyEmailUpdateRequest("00000", "hello@google.com");

        assertThatThrownBy(() -> service.verifyAndUpdateUserNotifyEmail(userContext, request))
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        e -> assertThat(e.getExceptionCode()).isEqualTo(ExceptionCode.EMAIL_VERIFICATION_CODE_NOT_FOUND)
                );
    }


}