package com.happypill.application.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerifyRequest(
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        String newEmail
) {
    public static EmailVerifyRequest of(String newEmail) {
        return new EmailVerifyRequest(newEmail);
    }
}
