package com.happypill.application.service.product.response;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.ProductPrice;

public record ProductInfoResponse(
        String language,
        String name,
        String briefDescription,
        String thumbnailUrl,
        String description,
        String contentImageUrl,
        Integer price,
        String company,
        Integer stock,
        Boolean isAvailable,
        String quantityDetails,
        String usage,
        String warningMessage

) {
    public static ProductInfoResponse from(Product product, ProductInfo productInfo, ProductPrice productPrice) {
        return new ProductInfoResponse(
                productInfo.getLanguage().toString(),
                productInfo.getName(),
                productInfo.getBriefDescription(),
                product.getThumbnailUrl(),
                productInfo.getDescription(),
                productInfo.getContentImage(),
                productPrice.getPrice(),
                productInfo.getCompany(),
                product.getStock(),
                product.isAvailable(),
                productInfo.getQuantityDetails(),
                productInfo.getUsage(),
                productInfo.getWarningMessage()
        );
    }

}