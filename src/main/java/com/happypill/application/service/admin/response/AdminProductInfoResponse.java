package com.happypill.application.service.admin.response;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.ProductPrice;
import com.happypill.application.service.product.response.ProductInfoResponse;

import java.util.List;

public record AdminProductInfoResponse(
        Long productId,
        Long categoryId,
        String thumbnailUrl,
        Boolean isAvailable,
        Integer stock,
        Integer price,
        List<ProductInfoResponse> productInfo
) {
    public static AdminProductInfoResponse from(Product product, List<ProductInfo> productInfos, ProductPrice productPrice){
        List<ProductInfoResponse> productInfoList = productInfos.stream()
                .map(ProductInfoResponse::from)
                .toList();

        return new AdminProductInfoResponse(
                product.getProductId(),
                product.getCategory().getCategoryId(),
                product.getThumbnailUrl(),
                product.isAvailable(),
                product.getStock(),
                productPrice.getPrice(),
                productInfoList
        );
    }
}
