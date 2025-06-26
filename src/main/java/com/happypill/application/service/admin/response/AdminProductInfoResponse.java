package com.happypill.application.service.admin.response;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductPrice;
import com.happypill.application.service.product.response.ProductInfoDetailsResponse;

import java.util.List;

public record AdminProductInfoResponse(
        String productId,
        String categoryId,
        String thumbnailUrl,
        boolean isAvailable,
        Integer stock,
        Integer price,
        List<ProductInfoDetailsResponse> productInfo
) {

    public static AdminProductInfoResponse fromEntities(Product product, List<ProductInfoDetailsResponse> productInfos, ProductPrice productPrice) {
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