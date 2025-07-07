package com.happypill.application.service.admin.response;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;

public record AdminCategoryListResponse(
        String categoryId,
        String name,
        String thumbnailUrl
) {
    public static AdminCategoryListResponse from(CategoryInfo categoryInfo, Category category){
        return new AdminCategoryListResponse(
                category.getId().toString(),
                categoryInfo.getName(),
                category.getThumbnailUrl()
        );
    }
}