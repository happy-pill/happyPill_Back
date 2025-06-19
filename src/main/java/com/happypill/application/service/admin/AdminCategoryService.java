package com.happypill.application.service.admin;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import com.happypill.application.service.admin.request.AdminCategoryInfoRequest;
import com.happypill.application.service.admin.request.AdminCategoryRequest;
import com.happypill.application.service.admin.response.AdminCategoryInfoResponse;
import com.happypill.application.service.admin.response.AdminCategoryListResponse;
import com.happypill.application.util.SnowflakeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryInfoRepository categoryInfoRepository;

    @Transactional(readOnly = true)
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

    public void saveCategories(AdminCategoryRequest adminCategoryCreateRequest) {
        boolean hasKoInfo = adminCategoryCreateRequest.categoryInfoRequests()
                .stream()
                .anyMatch(request -> Language.parseLanguage(request.language()) == Language.KO);

        if (!hasKoInfo) {
            throw new BusinessException(ExceptionCode.KO_LANGUAGE_REQUIRED);
        }

        List<Category> categories = new ArrayList<>();
        List<CategoryInfo> categoryInfos = new ArrayList<>();
        CategoryInfo categoryInfo;
        Language language;
        Category category = Category.of(SnowflakeUtil.nextId(), adminCategoryCreateRequest.thumbnailUrl(), adminCategoryCreateRequest.bannerImgUrl());
        categories.add(category);

        for (AdminCategoryInfoRequest request : adminCategoryCreateRequest.categoryInfoRequests()) {
            language = Language.parseLanguage(request.language());
            categoryInfo = CategoryInfo.of(SnowflakeUtil.nextId(), language, request.name(), request.description(), category);

            categoryInfos.add(categoryInfo);
        }

        categoryRepository.saveAll(categories);
        categoryInfoRepository.saveAll(categoryInfos);
    }

    @Transactional(readOnly = true)
    public AdminCategoryInfoResponse getCategoryDetails(Long categoryId){
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.CATEGORY_NOT_FOUND));

        List<CategoryInfo> categoryInfoList = categoryInfoRepository.getAllCategoryInfosById(category.getCategoryId());

        return AdminCategoryInfoResponse.fromCategoryAndInfos(category, categoryInfoList);
    }
}