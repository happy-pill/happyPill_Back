package com.happypill.application.service.category.dto.response;

import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;

public record CategoryInfoResponse(
        String categoryInfoId,
        Language language,
        String name,
        String description
) {
    public static CategoryInfoResponse fromEntity(CategoryInfo categoryInfo){
        return new CategoryInfoResponse(
                categoryInfo.getCategoryInfoId().toString(),
                categoryInfo.getLanguage(),
                categoryInfo.getName(),
                categoryInfo.getDescription()
        );
    }
}