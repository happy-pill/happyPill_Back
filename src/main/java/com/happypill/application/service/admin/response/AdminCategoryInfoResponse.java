package com.happypill.application.service.admin.response;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.service.category.dto.response.CategoryInfoResponse;

import java.util.List;

public record AdminCategoryInfoResponse(
        String categoryId,
        String thumbnailUrl,
        String bannerUrl,
        List<CategoryInfoResponse> categoryInfo
) {
    public static AdminCategoryInfoResponse fromCategoryAndInfos(Category category, List<CategoryInfo> categoryInfos){
        List<CategoryInfoResponse> categoryInfoList = categoryInfos.stream()
                .map(CategoryInfoResponse::fromEntity)
                .toList();

        return new AdminCategoryInfoResponse(
                category.getCategoryId().toString(),
                category.getThumbnailUrl(),
                category.getBannerUrl(),
                categoryInfoList
        );
    }
}