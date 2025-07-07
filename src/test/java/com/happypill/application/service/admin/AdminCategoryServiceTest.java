package com.happypill.application.service.admin;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.Product;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.service.admin.request.AdminCategoryInfoRequest;
import com.happypill.application.service.admin.request.AdminCategoryRequest;
import com.happypill.application.service.admin.request.AdminCategoryUpdateRequest;
import com.happypill.application.service.admin.response.AdminCategoryInfoResponse;
import com.happypill.application.service.admin.response.AdminCategoryListResponse;
import com.happypill.application.service.category.dto.response.CategoryNamesResponse;
import com.happypill.application.service.category.request.CategoryInfoRequest;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AdminCategoryServiceTest {

    @Autowired
    private AdminCategoryService adminCategoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryInfoRepository categoryInfoRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("[모든 카테고리 조회] 요청한 locale 에 따라 AdminCategoryListResponse 를 페이지네이션하여 반환한다.")
    void getAllCategories_1() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx");
        List<CategoryInfo> categoryInfoList = List.of(
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "카테고리명_KO", category),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "카테고리명_EN", category)
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
        List<AdminCategoryInfoRequest> categoryInfoRequests = List.of(new AdminCategoryInfoRequest("ko", "1번째 카테고리"));
        AdminCategoryRequest request = new AdminCategoryRequest("www.first_banner.com",
                "www.first_thumbnail.com", categoryInfoRequests);

        adminCategoryService.saveCategories(request);
        List<Category> categories = categoryRepository.findAllCategories();
        List<CategoryInfo> categoryInfos = categoryInfoRepository.findAllCategoryInfo();

        assertThat(categories.size()).isEqualTo(1);
        assertThat(categoryInfos.size()).isEqualTo(1);
        assertThat(categories.get(0).getThumbnailUrl()).isEqualTo(request.thumbnailUrl());
        assertThat(categoryInfos.get(0).getName()).isEqualTo(categoryInfoRequests.get(0).name());
    }

    @Test
    @DisplayName("새로운 카테고리 1개와 정보 2개 저장")
    void saveCategoryWithTwoInfo() {
        List<AdminCategoryInfoRequest> categoryInfoRequests = List.of(
                new AdminCategoryInfoRequest("ko", "1번째 카테고리"),
                new AdminCategoryInfoRequest("en", "first category"));
        AdminCategoryRequest request = new AdminCategoryRequest("www.first_banner.com",
                "www.first_thumbnail.com", categoryInfoRequests);

        adminCategoryService.saveCategories(request);
        List<Category> categories = categoryRepository.findAllCategories();
        List<CategoryInfo> categoryInfos = categoryInfoRepository.findAllCategoryInfo();

        assertThat(categories.size()).isEqualTo(1);
        assertThat(categoryInfos.size()).isEqualTo(2);
        assertThat(categories.get(0).getThumbnailUrl()).isEqualTo(request.thumbnailUrl());
        assertThat(categoryInfos.get(0).getName()).isEqualTo(categoryInfoRequests.get(0).name());
        assertThat(categoryInfos.get(1).getName()).isEqualTo(categoryInfoRequests.get(1).name());
    }

    @Test
    @DisplayName("[특정 카테고리 조회] 경로변수의 CategoryId 가 존재하지 않으면 에러가 발생한다.")
    void getCategoryDetails_1(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx");
        List<CategoryInfo> categoryInfoList = List.of(
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "카테고리명_KO", category),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "카테고리명_EN", category)
        );
        categoryRepository.save(category);
        categoryInfoRepository.saveAll(categoryInfoList);

        //when //then
        assertThatThrownBy(()->adminCategoryService.getCategoryDetails(1000L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[특정 카테고리 조회] 경로변수의 CategoryId 가 존재하면 200 상태코드와 함께 AdminCategoryInfoResponse 를 반환한다.")
    void getCategoryDetails_2(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx");
        List<CategoryInfo> categoryInfoList = List.of(
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "카테고리명_KO", category),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "카테고리명_EN", category)
        );
        categoryRepository.save(category);
        categoryInfoRepository.saveAll(categoryInfoList);

        //when
        AdminCategoryInfoResponse response = adminCategoryService.getCategoryDetails(category.getId());

        //then
        assertThat(response.categoryId()).isEqualTo(String.valueOf(category.getId()));
    }

    @Test
    @DisplayName("[카테고리 목록 조회] 정상적으로 카테고리 리스트를 반환한다.")
    void getCategoryList_1(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx");
        List<CategoryInfo> categoryInfoList = List.of(
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "카테고리명_KO", category),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "카테고리명_EN", category)
        );
        categoryRepository.save(category);
        categoryInfoRepository.saveAll(categoryInfoList);

        //when
        List<CategoryNamesResponse> responses = adminCategoryService.getCategoryNames();

        //then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).categoryId()).isEqualTo(String.valueOf(category.getId()));
    }

    @Test
    @DisplayName("[카테고리 수정] 경로 변수의 categoryId 가 존재하지 않는 Category 면 예외가 발생한다.")
    void updateCategory_1(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx");
        List<CategoryInfo> categoryInfoList = List.of(
                CategoryInfo.of(1L, Language.KO, "카테고리명_KO", category),
                CategoryInfo.of(2L, Language.EN, "카테고리명_EN", category)
        );
        categoryRepository.save(category);
        categoryInfoRepository.saveAll(categoryInfoList);

        List<CategoryInfoRequest> infoRequests = List.of(
                new CategoryInfoRequest(1L, Language.KO, "변경된_카테고리명_KO"),
                new CategoryInfoRequest(2L, Language.EN, "변경된_카테고리명_EN")
        );
        AdminCategoryUpdateRequest updateRequest = new AdminCategoryUpdateRequest("https://xxx.com/xxx", "https://xxxxx.com/xxxxx", infoRequests);

        //when //then
        assertThatThrownBy(() -> adminCategoryService.updateCategory(0L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[카테고리 수정] CategoryInfoRequest 의 categoryInfoId 가 category 에 속해있지 않는 값이면 예외가 발생한다.")
    void updateCategory_2(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx");
        List<CategoryInfo> categoryInfoList = List.of(
                CategoryInfo.of(1L, Language.KO, "카테고리명_KO", category),
                CategoryInfo.of(2L, Language.EN, "카테고리명_EN", category)
        );
        categoryRepository.save(category);
        categoryInfoRepository.saveAll(categoryInfoList);

        List<CategoryInfoRequest> infoRequests = List.of(
                new CategoryInfoRequest(3L, Language.KO, "변경된_카테고리명_KO"),
                new CategoryInfoRequest(4L, Language.EN, "변경된_카테고리명_EN")
        );
        AdminCategoryUpdateRequest updateRequest = new AdminCategoryUpdateRequest("https://xxx.com/xxx", "https://xxxxx.com/xxxxx", infoRequests);

        //when //then
        assertThatThrownBy(() -> adminCategoryService.updateCategory(category.getId(), updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.CATEGORY_INFO_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[카테고리 수정] 언어가 KO로 된 CategoryInfo 만 존재할 때 EN로 된 CategoryInfo 정보를 작성할 경우 EN 으로 된 CategoryInfo 객체가 새로 생성된다.")
    void updateCategory_3(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx");
        CategoryInfo categoryInfo = CategoryInfo.of(1L, Language.KO, "카테고리명_KO", category);

        categoryRepository.save(category);
        categoryInfoRepository.save(categoryInfo);

        List<CategoryInfoRequest> infoRequests = List.of(
                new CategoryInfoRequest(1L, Language.KO, "변경된_카테고리명_KO"),
                new CategoryInfoRequest(null, Language.EN, "추가된_카테고리명_EN")
        );
        AdminCategoryUpdateRequest updateRequest = new AdminCategoryUpdateRequest("https://xxx.com/xxx", "https://xxxxx.com/xxxxx", infoRequests);

        //when
        AdminCategoryInfoResponse response = adminCategoryService.updateCategory(category.getId(), updateRequest);
        List<CategoryInfo> categoryInfos = categoryInfoRepository.findAllByCategory(category);

        // then
        assertThat(categoryInfos).hasSize(2);
    }

    @Test
    @DisplayName("[카테고리 삭제] 경로변수의 CategoryId 가 존재하지 않는 값일 때 예외를 반환한다.")
    void deleteCategory_1(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx");
        List<CategoryInfo> categoryInfoList = List.of(
                CategoryInfo.of(1L, Language.KO, "카테고리명_KO", category),
                CategoryInfo.of(2L, Language.EN, "카테고리명_EN", category)
        );

        categoryRepository.save(category);
        categoryInfoRepository.saveAll(categoryInfoList);

        //when //then
        assertThatThrownBy(() -> adminCategoryService.deleteCategory(0L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[카테고리 삭제] 카테고리가 삭제될 때 그 카테고리와 관련된 상품들의 category 필드는 null 값으로 변경된다.")
    void deleteCategory_2(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx");
        List<CategoryInfo> categoryInfoList = List.of(
                CategoryInfo.of(1L, Language.KO, "카테고리명_KO", category),
                CategoryInfo.of(2L, Language.EN, "카테고리명_EN", category)
        );
        Product product = Product.of(SnowflakeUtil.nextId(), 3500, 5, "https://xxxxx.com/xxxxx", category);

        categoryRepository.save(category);
        categoryInfoRepository.saveAll(categoryInfoList);
        productRepository.save(product);

        //when
        adminCategoryService.deleteCategory(category.getId());

        //then
        assertThat(product.getCategory()).isNull();
    }

    @Test
    @DisplayName("[카테고리 삭제] 카테고리가 삭제될 때 그 카테고리와 관련된 CategoryInfo 객체들은 모두 DB 내에서 삭제된다.")
    void deleteCategory_3(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx");
        List<CategoryInfo> categoryInfoList = List.of(
                CategoryInfo.of(1L, Language.KO, "카테고리명_KO", category),
                CategoryInfo.of(2L, Language.EN, "카테고리명_EN", category)
        );

        categoryRepository.save(category);
        categoryInfoRepository.saveAll(categoryInfoList);

        //when
        adminCategoryService.deleteCategory(category.getId());

        //then
        for(CategoryInfo ci : categoryInfoList){
            boolean categoryInfoExists = categoryInfoRepository.existsById(ci.getId());
            assertThat(categoryInfoExists).isFalse();
        }

        boolean categoryExists = categoryRepository.existsById(category.getId());
        assertThat(categoryExists).isFalse();
    }
}