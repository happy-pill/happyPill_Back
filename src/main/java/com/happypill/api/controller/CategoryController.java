package com.happypill.api.controller;

import com.happypill.application.entity.enums.Language;
import com.happypill.application.service.CategoryService;
import com.happypill.application.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getCategories(@RequestHeader("Language") String headerLanguage) {
        Language language = Language.parseLanguage(headerLanguage);
        return categoryService.getAllCategories(language);
    }
}
