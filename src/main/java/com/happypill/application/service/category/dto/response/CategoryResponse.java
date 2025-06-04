package com.happypill.application.service.category.dto.response;

public record CategoryResponse (
     Long categoryId,
     String thumbnailUrl,
     String name,
     String description,
     String bannerImgUrl
) {
}