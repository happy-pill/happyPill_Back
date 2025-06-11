package com.happypill.application.service.admin.response;

import com.happypill.application.entity.ProductPrice;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public record AdminProductPriceResponse(
        ZonedDateTime date,
        Integer price,
        boolean isUsed
) {
    public static AdminProductPriceResponse from(ProductPrice price){
        return new AdminProductPriceResponse(
                price.getCreatedAt(),
                price.getPrice(),
                price.isUsed()
        );
    }
}