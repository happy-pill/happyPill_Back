package com.happypill.application.service.product.dto.response;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;

public record ProductResponse(String productId, Long categoryId, String name, String company, int price,
                              String briefDescription, String thumbnailUrl)
{
    public static ProductResponse from(ProductInfo productInfo) {
        Product product = productInfo.getProduct();
        Category category = product.getCategory();

        return new ProductResponse(product.getProductId().toString(), category.getCategoryId(), productInfo.getName(),
                productInfo.getCompany(), product.getPrice(), productInfo.getBriefDescription(), product.getThumbnailUrl());
    }
}
