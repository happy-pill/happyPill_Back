package com.happypill.application.service;

import com.happypill.application.dto.response.CategoryResponse;
import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryInfoRepository categoryInfoRepository;

    public List<CategoryResponse> getAllCategories(Locale locale) {
        Language language = Language.parseLanguage(locale.getLanguage());
        List<CategoryResponse> results = new ArrayList<>();
        List<CategoryInfo> categoryInfoList = categoryInfoRepository.findByLanguage(language);
        Category category = null;
        CategoryResponse response = null;

        for (CategoryInfo categoryInfo : categoryInfoList) {
            category = categoryInfo.getCategory();
            response = new CategoryResponse(category.getCategoryId(), category.getThumbnailUrl(),
                    categoryInfo.getName(), categoryInfo.getDescription(), category.getBannerUrl());
            results.add(response);
        }

        return results;
    }
}
