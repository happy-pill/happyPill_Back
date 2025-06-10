package com.happypill.application.service.admin.request;

import com.happypill.application.service.product.request.ProductInfoRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AdminProductUpdateRequest(

        @NotNull(message = "카테고리를 선택해주세요.")
        String categoryId,

        @NotBlank(message = "대표 사진은 필수입니다.")
        String thumbnailUrl,

        boolean isAvailable,

        @NotNull(message = "재고수량은 필수 입력란입니다.")
        @PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다.")
        Integer stock,

        @NotNull(message = "가격 정보는 필수 입력값입니다.")
        @PositiveOrZero(message = "가격은 0 이상의 값 이어야 합니다.")
        Integer price,

        @NotNull(message = "상품 정보는 필수 입력값 입니다.")
        @Size(min = 1, message = "상품 정보는 1개 이상 등록되어야 합니다.")
        List<@Valid ProductInfoRequest> productInfos
) {
}