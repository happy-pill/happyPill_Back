package com.happypill.application.service.product.request;

import com.happypill.application.entity.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductInfoRequest(

        @NotNull(message = "언어는 필수입니다.")
        Language language,

        @NotBlank(message = "상품이름은 필수 입력란입니다.")
        String name,

        @NotBlank(message = "간단설명은 필수 입력란입니다.")
        String briefDescription,

        @NotBlank(message = "상세설명은 필수 입력란입니다.")
        String description,

        @NotNull(message = "상세정보 이미지는 필수입니다.")
        String contentImageUrl,

        @NotBlank(message = "제조사는 필수 입력란입니다.")
        String company,

        @NotBlank(message = "용량/수량은 필수 입력란입니다.")
        String quantityDetails,

        String usage,

        String warningMessage
) { }