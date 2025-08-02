package com.happypill.application.service.admin.response;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class AdminCategoryListResponse
{
    private String categoryId;
    private String name;
    private String description;
    private String thumbnailUrl;
    private String bannerImgUrl;
    private int numOfProducts;

    @QueryProjection
    public AdminCategoryListResponse(
            Long categoryId,
            String name,
            String description,
            String thumbnailUrl,
            String bannerImgUrl,
            int numOfProducts
    ) {
        this.categoryId = String.valueOf(categoryId);
        this.name = name;
        this.description = "";
        this.thumbnailUrl = thumbnailUrl;
        this.bannerImgUrl = "";
        this.numOfProducts = numOfProducts;
    }

public static AdminCategoryListResponse from(CategoryInfo categoryInfo, Category category, int numOfProducts){
        return new AdminCategoryListResponse(
                category.getId(),
                categoryInfo.getName(),
                categoryInfo.getDescription(),
                category.getThumbnailUrl(),
                category.getBannerUrl(),
                numOfProducts
        );
    }
}