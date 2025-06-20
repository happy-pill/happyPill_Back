package com.happypill.application.service.product.response;

import com.happypill.application.entity.Product;

public record ProductRelatedResponse (String productId, String thumbnailUrl, int price)
{
    public static ProductRelatedResponse from (Product product) {
        return new ProductRelatedResponse(
                product.getId().toString(),
                product.getThumbnailUrl(),
                product.getPrice()
        );
    }
}
