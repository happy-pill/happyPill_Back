package com.happypill.application.service.admin.response;

import com.querydsl.core.annotations.QueryProjection;

public record AdminProductResponse(
        String productId,
        String categoryId,
        String productName,
        String categoryName,
        String company,
        Integer price,
        Integer stock,
        String briefDescription,
        String thumbnailUrl,
        boolean isAvailable
) {

    @QueryProjection
    public AdminProductResponse(
            Long productId,
            Long categoryId,
            String productName,
            String categoryName,
            String company,
            Integer price,
            Integer stock,
            String briefDescription,
            String thumbnailUrl,
            boolean isAvailable
    ) {
        this(String.valueOf(productId),
                String.valueOf(categoryId),
                productName,
                categoryName,
                company,
                price,
                stock,
                briefDescription,
                thumbnailUrl,
                isAvailable);
    }

}
