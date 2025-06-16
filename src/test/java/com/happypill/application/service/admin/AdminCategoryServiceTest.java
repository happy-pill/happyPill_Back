package com.happypill.application.service.admin;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import com.happypill.application.service.admin.request.AdminCategoryInfoRequest;
import com.happypill.application.service.admin.request.AdminCategoryRequest;
import com.happypill.application.service.admin.response.AdminCategoryListResponse;
import com.happypill.application.util.SnowflakeUtil;
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
class AdminCategoryServiceTest {

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

    @Test
    @DisplayName("새로운 카테고리 1개와 정보 1개 저장")
    void saveCategoryWithOneInfo() {
        List<AdminCategoryInfoRequest> categoryInfoRequests = List.of(new AdminCategoryInfoRequest("ko", "1번째 카테고리", "첫 카테고리 생성"));
        AdminCategoryRequest request = new AdminCategoryRequest("www.first_banner.com",
                "www.first_thumbnail.com", categoryInfoRequests);

        adminCategoryService.saveCategories(request);
        List<Category> categories = categoryRepository.findAllCategories();
        List<CategoryInfo> categoryInfos = categoryInfoRepository.findAllCategoryInfo();

        assertThat(categories.size()).isEqualTo(1);
        assertThat(categoryInfos.size()).isEqualTo(1);
        assertThat(categories.get(0).getThumbnailUrl()).isEqualTo(request.thumbnailUrl());
        assertThat(categories.get(0).getBannerUrl()).isEqualTo(request.bannerImgUrl());
        assertThat(categoryInfos.get(0).getName()).isEqualTo(categoryInfoRequests.get(0).name());
        assertThat(categoryInfos.get(0).getDescription()).isEqualTo(categoryInfoRequests.get(0).description());
    }

    @Test
    @DisplayName("새로운 카테고리 1개와 정보 2개 저장")
    void saveCategoryWithTwoInfo() {
        List<AdminCategoryInfoRequest> categoryInfoRequests = List.of(
                new AdminCategoryInfoRequest("ko", "1번째 카테고리", "첫 카테고리 생성"),
                new AdminCategoryInfoRequest("en", "first category", "First category created"));
        AdminCategoryRequest request = new AdminCategoryRequest("www.first_banner.com",
                "www.first_thumbnail.com", categoryInfoRequests);

        adminCategoryService.saveCategories(request);
        List<Category> categories = categoryRepository.findAllCategories();
        List<CategoryInfo> categoryInfos = categoryInfoRepository.findAllCategoryInfo();

        assertThat(categories.size()).isEqualTo(1);
        assertThat(categoryInfos.size()).isEqualTo(2);
        assertThat(categories.get(0).getThumbnailUrl()).isEqualTo(request.thumbnailUrl());
        assertThat(categories.get(0).getBannerUrl()).isEqualTo(request.bannerImgUrl());
        assertThat(categoryInfos.get(0).getName()).isEqualTo(categoryInfoRequests.get(0).name());
        assertThat(categoryInfos.get(0).getDescription()).isEqualTo(categoryInfoRequests.get(0).description());
        assertThat(categoryInfos.get(1).getName()).isEqualTo(categoryInfoRequests.get(1).name());
        assertThat(categoryInfos.get(1).getDescription()).isEqualTo(categoryInfoRequests.get(1).description());
    }
}