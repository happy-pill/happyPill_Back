package com.happypill.application.service.category.request;

import com.happypill.application.entity.enums.Language;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CategoryInfoRequest(

        Long categoryInfoId,

        @NotNull(message = "언어는 필수 입력란입니다")
        Language language,

        @NotEmpty(message = "이름은 필수 입력란입니다")
        String name
) {
}