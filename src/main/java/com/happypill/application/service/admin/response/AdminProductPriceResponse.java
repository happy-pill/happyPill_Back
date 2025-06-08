package com.happypill.application.service.admin.response;

import com.happypill.application.entity.ProductPrice;

import java.time.format.DateTimeFormatter;

public record AdminProductPriceResponse(
        String date,
        Integer price,
        boolean isUsed
) {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static AdminProductPriceResponse from(ProductPrice price){
        String formattedDate = price.getCreatedAt().format(formatter);
        return new AdminProductPriceResponse(
                formattedDate,
                price.getPrice(),
                price.isUsed()
        );
    }
}