package com.happypill.application.dto.response;

public record CategoryResponse (
     Long categoryId,
     String thumbnailUrl,
     String name,
     String description,
     String bannerImgUrl
) {

    public static CategoryResponse of(Long categoryId, String thumbnailUrl, String name,
                                                       String description, String bannerImgUrl) {
        return new CategoryResponse(categoryId, thumbnailUrl, name, description, bannerImgUrl);
    }
}