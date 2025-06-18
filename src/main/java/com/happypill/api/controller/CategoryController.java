package com.happypill.api.controller;

import com.happypill.application.service.category.CategoryService;
import com.happypill.application.service.category.dto.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@Tag(name = "카테고리", description = "카테고리 정보를 조회하기 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private static final String LANGUAGE_HEADER = "Language";
    private final CategoryService categoryService;

    @Operation(summary = "모든 카테고리 조회", description = "카테고리 리스트를 조회합니다.")
    @GetMapping
    public List<CategoryResponse> getCategories(@RequestHeader(LANGUAGE_HEADER) String headerLanguage) {
        Locale locale = Locale.of(headerLanguage);
        return categoryService.getAllCategories(locale);
    }
}