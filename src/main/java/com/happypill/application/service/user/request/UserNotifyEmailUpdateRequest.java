package com.happypill.application.service.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserNotifyEmailUpdateRequest(
        @NotBlank(message = "인증 코드가 제공되지 않았습니다.")
        String verifyCode,
        @NotNull(message = "변경할 이메일이 제공되지 않았습니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다")
        String notifyEmail
) {
}
