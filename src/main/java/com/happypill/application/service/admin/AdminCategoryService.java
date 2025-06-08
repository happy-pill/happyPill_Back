package com.happypill.application.service.admin;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import com.happypill.application.service.admin.response.AdminCategoryListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryInfoRepository categoryInfoRepository;

    //모든 카테고리 조회
    public CustomPage<AdminCategoryListResponse> getAllCategories(Pageable pageable, Locale locale) {
        Language language = Language.parseLanguage(locale.getLanguage());

        Page<CategoryInfo> categoryInfos = categoryInfoRepository.getAllCategoryInfo(pageable, language);

        Page<AdminCategoryListResponse> responsePage = categoryInfos.map(categoryInfo -> {
                    Category category = categoryInfo.getCategory();
                    return AdminCategoryListResponse.from(categoryInfo, category);
                }
        );
        return new CustomPage<>(responsePage);
    }
}