package com.happypill.application.service.admin.response;

public record AdminProductListResponse(
        Long productId,
        Long categoryId,
        String name,
        String company,
        Integer price,
        String briefDescription,
        String thumbnailUrl,
        boolean isAvailable
) {
}