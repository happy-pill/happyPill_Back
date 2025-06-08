package com.happypill.application.service.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductInfoResponse (Long productId, String name, String company, int price, String briefDescription,
                                   String description, String thumbnailUrl, String contentImageUrl, String quantityDetails,
                                   String usage, String warningMessage) {

    public static ProductInfoResponse from (Product product, ProductInfo productInfo, int price) {
        return new ProductInfoResponse(
                product.getProductId(),
                productInfo.getName(),
                productInfo.getCompany(),
                price,
                productInfo.getBriefDescription(),
                productInfo.getDescription(),
                product.getThumbnailUrl(),
                productInfo.getContentImageUrl(),
                productInfo.getQuantityDetails(),
                productInfo.getUsage(),
                productInfo.getWarningMessage()
        );
    }

    public static ProductInfoResponse from (Product product, int price) {
        return new ProductInfoResponse(
                product.getProductId(),
                null,
                null,
                price,
                null,
                null,
                product.getThumbnailUrl(),
                null,
                null,
                null,
                null
        );
    }
}