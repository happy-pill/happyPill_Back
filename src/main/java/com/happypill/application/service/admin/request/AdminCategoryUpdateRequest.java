package com.happypill.application.service.admin.request;

import com.happypill.application.service.category.request.CategoryInfoRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AdminCategoryUpdateRequest(

        @NotNull(message = "썸네일사진은 필수 입력란입니다")
        String thumbnailUrl,

        @NotNull(message = "배너사진은 필수 입력란입니다")
        String bannerUrl,

        @Size(min = 1, message = "카테고리 정보는 필수 입력란입니다")
        List<@Valid CategoryInfoRequest> categoryInfos
) {
}
