package com.happypill.application.service.category.dto.response;

public record CategoryResponse (
     String categoryId,
     String thumbnailUrl,
     String name,
     String description,
     String bannerImgUrl
) {
}