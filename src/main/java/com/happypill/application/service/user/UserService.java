package com.happypill.application.service.user;

import com.happypill.application.auth.UserContext;
import com.happypill.application.email.EmailSender;
import com.happypill.application.entity.HappypillUser;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.service.user.email.EmailVerificationRedisRepository;
import com.happypill.application.service.user.request.EmailVerificationRequest;
import com.happypill.application.service.user.request.UserNotifyEmailUpdateRequest;
import com.happypill.application.service.user.request.UserUpdateRequest;
import com.happypill.application.service.user.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.happypill.application.exception.custom.ExceptionCode.EMAIL_VERIFICATION_CODE_NOT_FOUND;
import static com.happypill.application.exception.custom.ExceptionCode.USER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private static final int EMAIL_REQUEST_COUNT_LIMIT = 3;

    private static final Duration EMAIL_REQUEST_COUNT_TTL = Duration.ofDays(1);

    private static final Duration VERIFY_CODE_TTL = Duration.ofMinutes(3);

    private final HappypillUserRepository userRepository;

    private final EmailVerificationRedisRepository emailVerificationRedisRepository;

    private final EmailSender emailSender;

    private final VerifyCodeGenerator verifyCodeGenerator;

    public UserInfoResponse getUserInfo(UserContext userContext) {
        HappypillUser user = userRepository.findById(userContext.getId())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        return UserInfoResponse.from(user);
    }

    @Transactional
    public UserInfoResponse updateUser(UserContext userContext, UserUpdateRequest userUpdateRequest) {
        HappypillUser user = userRepository.findById(userContext.getId())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        user.changeUser(userUpdateRequest.nickName());
        return UserInfoResponse.from(user);
    }

    public void sendEmailVerificationCode(UserContext userContext, EmailVerificationRequest request) {
        Long count = emailVerificationRedisRepository.increaseRequestCount(userContext.getId(), EMAIL_REQUEST_COUNT_TTL);
        if (count > EMAIL_REQUEST_COUNT_LIMIT) {
            throw new BusinessException(ExceptionCode.EMAIL_REQUEST_COUNT_EXCEED);
        }
        String verifyCode = verifyCodeGenerator.generate();
        emailVerificationRedisRepository.saveVerificationCode(userContext.getId(), request.notifyEmail(), verifyCode, VERIFY_CODE_TTL);
        emailSender.send(request.notifyEmail(), verifyCode); // TODO ASYNC
    }

    @Transactional
    public void verifyAndUpdateUserNotifyEmail(UserContext userContext, UserNotifyEmailUpdateRequest request) {
        verifyNotifyEmail(userContext, request);
        HappypillUser user = userRepository.findById(userContext.getId())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        user.changeNotifyEmail(request.notifyEmail());
    }

    private void verifyNotifyEmail(UserContext userContext, UserNotifyEmailUpdateRequest request) {
        emailVerificationRedisRepository.getVerificationCode(userContext.getId(), request.notifyEmail())
                .filter(code -> request.verifyCode().equals(code))
                .orElseThrow(() -> new BusinessException(EMAIL_VERIFICATION_CODE_NOT_FOUND));
    }

}
