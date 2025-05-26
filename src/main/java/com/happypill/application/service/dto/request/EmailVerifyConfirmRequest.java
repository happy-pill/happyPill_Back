package com.happypill.application.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailVerifyConfirmRequest(
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        String newEmail,

        @NotBlank(message = "인증코드는 필수 입력값입니다.")
        @Pattern(regexp = "\\d{6}", message = "인증코드는 6자리 숫자여야 합니다.")
        String code
) {
        public static EmailVerifyConfirmRequest of(String newEmail, String code){
                return new EmailVerifyConfirmRequest(newEmail, code);
        }
}
