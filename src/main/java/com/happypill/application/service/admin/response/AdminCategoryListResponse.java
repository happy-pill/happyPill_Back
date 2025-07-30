package com.happypill.application.service.admin.response;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;

public record AdminCategoryListResponse(
        String categoryId,
        String name,
        String description,
        String thumbnailUrl,
        String bannerImgUrl,
        int numOfProducts
) {
    public static AdminCategoryListResponse from(CategoryInfo categoryInfo, Category category, int numOfProducts){
        return new AdminCategoryListResponse(
                category.getId().toString(),
                categoryInfo.getName(),
                categoryInfo.getDescription(),
                category.getThumbnailUrl(),
                category.getBannerUrl(),
                numOfProducts
        );
    }
}