package com.happypill.api.controller;

import com.happypill.application.service.category.dto.response.CategoryResponse;
import com.happypill.application.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private static final String LANGUAGE_HEADER = "Language";
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getCategories(@RequestHeader(LANGUAGE_HEADER) String headerLanguage) {
        Locale locale = Locale.of(headerLanguage);
        return categoryService.getAllCategories(locale);
    }
}
