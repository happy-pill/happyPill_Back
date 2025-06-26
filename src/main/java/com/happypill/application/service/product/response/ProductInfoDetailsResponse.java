package com.happypill.application.service.product.response;

import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.enums.Language;

public record ProductInfoDetailsResponse(
        Language language,
        String name,
        String briefDescription,
        String description,
        String contentImageUrl,
        String company,
        String quantityDetails,
        String usage,
        String warningMessage

) {
    public static ProductInfoDetailsResponse from(ProductInfo productInfo) {
        return new ProductInfoDetailsResponse(
                productInfo.getLanguage(),
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