package com.happypill.application.service.product.dto.response;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;

public record ProductInfoResponse (String productId, String name, String company, int price, String briefDescription,
                                   String description, String thumbnailUrl, String contentImageUrl, String quantityDetails,
                                   String usage, String warningMessage) {

    public static ProductInfoResponse from(Product product, ProductInfo productInfo) {
        return new ProductInfoResponse(
                product.getProductId().toString(),
                productInfo.getName(),
                productInfo.getCompany(),
                product.getPrice(),
                productInfo.getBriefDescription(),
                productInfo.getDescription(),
                product.getThumbnailUrl(),
                productInfo.getContentImageUrl(),
                productInfo.getQuantityDetails(),
                productInfo.getUsage(),
                productInfo.getWarningMessage()
        );
    }
}