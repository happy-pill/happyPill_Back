package com.happypill.application.service;

import com.happypill.application.dto.response.CategoryResponse;
import com.happypill.application.entity.Category;
import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryInfoRepository categoryInfoRepository;

    @Test
    @DisplayName("카테고리 있을 때 불러오기 테스트")
    public void shouldReturnCategories() {
        Language language = Language.KO;
        Category category = Category.of(1L, "first thum url", "first banner url");
        List<CategoryInfo> categoryResponses = Arrays.asList(
                CategoryInfo.of(1L, Language.KO, "first name", "first desc", category),
                CategoryInfo.of(2L, Language.KO, "second name", "second desc", category)
        );

        BDDMockito.given(categoryInfoRepository.findByLanguage(language)).willReturn(categoryResponses);

        List<CategoryResponse> result = categoryService.getAllCategories(language);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("카테고리 없을 때 불러오기 테스트")
    public void shouldReturnEmptyDto() {
        Language language = Language.KO;
        List<CategoryInfo> categoryResponses = new ArrayList<>();

        BDDMockito.given(categoryInfoRepository.findByLanguage(language)).willReturn(categoryResponses);

        List<CategoryResponse> result = categoryService.getAllCategories(language);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
    }
}