package com.happypill.application.service.product.response;

import com.happypill.application.entity.ProductInfo;

public record ProductInfoResponse(
        String language,
        String name,
        String briefDescription,
        String description,
        String contentImageUrl,
        String company,
        String quantityDetails,
        String usage,
        String warningMessage

) {
    public static ProductInfoResponse from(ProductInfo productInfo) {
        return new ProductInfoResponse(
                productInfo.getLanguage().toString(),
                productInfo.getName(),
                productInfo.getBriefDescription(),
                productInfo.getDescription(),
                productInfo.getContentImageUrl(),
                productInfo.getCompany(),
                productInfo.getQuantityDetails(),
                productInfo.getUsage(),
                productInfo.getWarningMessage()
        );
    }
}