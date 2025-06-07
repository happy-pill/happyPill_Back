package com.happypill.application.service.admin.response;

import com.happypill.application.entity.ProductPrice;

import java.time.format.DateTimeFormatter;

public record AdminProductPriceResponse(
        String date,
        Integer price,
        Boolean isUsed
) {
    public static AdminProductPriceResponse from(ProductPrice price){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedDate = price.getCreatedAt().format(formatter);
        return new AdminProductPriceResponse(
                formattedDate,
                price.getPrice(),
                price.isUsed()
        );
    }
}