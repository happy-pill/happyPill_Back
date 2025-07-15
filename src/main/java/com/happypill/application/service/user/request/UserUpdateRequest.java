package com.happypill.application.service.user.request;

import com.happypill.application.validation.ValidNickname;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank(message = "닉네임은 비어 있을 수 없습니다")
        @ValidNickname
        String nickName
) {
}
