package com.happypill.application.service.admin.response;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;

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
    public static AdminProductListResponse from(Product product, ProductInfo productInfo, int price) {
        return new AdminProductListResponse(
                product.getProductId(),
                product.getCategory().getCategoryId(),
                productInfo.getName(),
                productInfo.getCompany(),
                price,
                productInfo.getBriefDescription(),
                product.getThumbnailUrl(),
                product.isAvailable()
        );
    }
}