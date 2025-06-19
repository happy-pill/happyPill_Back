package com.happypill.application.service.category.dto.response;

import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;

import java.util.List;
import java.util.Map;

public record CategoryNamesResponse(
        String categoryId,
        List<LocalizedName> names
) {
    public record LocalizedName(
            Language language,
            String name
    ){ }

    public static CategoryNamesResponse fromCategoryIdAndInfos(Long categoryId, List<CategoryInfo> infos){
        List<LocalizedName> localizedNames = infos.stream()
                .map(ci -> new LocalizedName(ci.getLanguage(), ci.getName()))
                .toList();

        return new CategoryNamesResponse(String.valueOf(categoryId), localizedNames);
    }
}