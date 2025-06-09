package com.happypill.application.service.admin;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import com.happypill.application.service.admin.response.AdminCategoryListResponse;
import com.happypill.application.util.SnowflakeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AdminCategoryControllerTest {

    @Autowired
    private AdminCategoryService adminCategoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryInfoRepository categoryInfoRepository;

    @Test
    @DisplayName("[모든 카테고리 조회] 요청한 locale 에 따라 AdminCategoryListResponse 를 페이지네이션하여 반환한다.")
    void getAllCategories_1() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx", "https://xxxxx.com/xxxxx");
        List<CategoryInfo> categoryInfoList = List.of(
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "카테고리명_KO", "설명_KO", category),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "카테고리명_EN", "설명_EN", category)
        );
        categoryRepository.save(category);
        categoryInfoRepository.saveAll(categoryInfoList);

        Pageable pageable = PageRequest.of(0, 5);
        Locale locale = Locale.KOREA;

        //when
        CustomPage<AdminCategoryListResponse> result = adminCategoryService.getAllCategories(pageable, locale);

        //then
        assertThat(result.contents())
                .extracting(AdminCategoryListResponse::name)
                .anyMatch(name -> name.contains("KO"));
    }
}