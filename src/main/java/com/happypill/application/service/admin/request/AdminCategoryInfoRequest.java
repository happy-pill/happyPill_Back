package com.happypill.application.service.admin.request;

import jakarta.validation.constraints.NotNull;

public record AdminCategoryInfoRequest (

        @NotNull(message = "언어는 필수 입력란입니다")
        String language,

        @NotNull(message = "이름은 필수 입력란입니다")
        String name
){
}