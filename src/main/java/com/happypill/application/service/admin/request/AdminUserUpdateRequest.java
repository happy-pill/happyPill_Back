package com.happypill.application.service.admin.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record AdminUserUpdateRequest(
        @Size(min = 2, max = 10, message = "닉네임은 최소 2자 이상 10자 이하여야 합니다.")
        String nickName,

        @Email(message = "이메일 형식이어야 합니다.")
        String notifyEmail
) {
}