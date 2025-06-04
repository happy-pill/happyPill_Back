package com.happypill.application.service.category;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import com.happypill.application.service.category.dto.response.CategoryResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Before All 때문
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryInfoRepository categoryInfoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void clearDB() {
        categoryInfoRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 있을 때 불러오기 테스트")
    public void shouldReturnCategories() {
        Locale locale = Locale.of("KO");
        Category category = Category.of(1L, "first thum url", "first banner url");
        List<CategoryInfo> categoryResponses = Arrays.asList(
                CategoryInfo.of(1L, Language.KO, "first name", "first desc", category),
                CategoryInfo.of(2L, Language.KO, "second name", "second desc", category)
        );

        categoryRepository.save(category);
        categoryInfoRepository.saveAll(categoryResponses);

        List<CategoryResponse> result = categoryService.getAllCategories(locale);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("카테고리 없을 때 불러오기 테스트")
    public void shouldReturnEmptyDto() {
        Locale locale = Locale.of("KO");

        List<CategoryResponse> result = categoryService.getAllCategories(locale);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
    }
}