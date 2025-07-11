package com.happypill.application.service.product.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@NoArgsConstructor
@Getter
public class ProductListResponse {
    private String productId;
    private String categoryId;
    private String productName;
    private String categoryName;
    private String company;
    private int price;
    private String briefDescription;
    private String thumbnailUrl;
    private boolean isBest;

    @QueryProjection
    public ProductListResponse(String productId, String categoryId, String productName, String categoryName, String company, int price, String briefDescription, String thumbnailUrl, boolean isBest) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.company = company;
        this.price = price;
        this.briefDescription = briefDescription;
        this.thumbnailUrl = thumbnailUrl;
        this.isBest = isBest;
    }
}
