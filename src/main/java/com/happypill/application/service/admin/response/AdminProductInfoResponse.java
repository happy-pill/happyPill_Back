package com.happypill.application.service.admin.response;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductPrice;
import com.happypill.application.service.product.response.ProductInfoResponse;

import java.util.List;

public record AdminProductInfoResponse(
        String productId,
        String categoryId,
        String thumbnailUrl,
        boolean isAvailable,
        Integer stock,
        Integer price,
        List<ProductInfoResponse> productInfo
) {

    public static AdminProductInfoResponse from(Product product, List<ProductInfoResponse> productInfos, ProductPrice productPrice) {
        return new AdminProductInfoResponse(
                product.getId().toString(),
                product.getCategory().getId().toString(),
                product.getThumbnailUrl(),
                product.isAvailable(),
                product.getStock(),
                productPrice.getPrice(),
                productInfos
        );
    }
}