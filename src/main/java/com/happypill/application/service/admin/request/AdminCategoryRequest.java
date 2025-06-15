package com.happypill.application.service.admin.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AdminCategoryRequest (

        @NotNull(message = "배너사진은 필수 입력란입니다")
        String bannerImgUrl,

        @NotNull(message = "썸네일사진은 필수 입력란입니다")
        String thumbnailUrl,

        @Size(min = 1, message = "카테고리 정보는 필수 입력란입니다")
        List<AdminCategoryInfoRequest> categoryInfoRequests
) {
}