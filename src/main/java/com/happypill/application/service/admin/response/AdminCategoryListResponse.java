package com.happypill.application.service.admin.response;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;

public record AdminCategoryListResponse(
        String categoryId,
        String name,
        String description,
        String thumbnailUrl,
        String bannerImgUrl
) {
    public static AdminCategoryListResponse from(CategoryInfo categoryInfo, Category category){
        return new AdminCategoryListResponse(
                category.getCategoryId().toString(),
                categoryInfo.getName(),
                categoryInfo.getDescription(),
                category.getThumbnailUrl(),
                category.getBannerUrl()
        );
    }
}