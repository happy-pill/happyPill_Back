package com.happypill.api.controller.admin;

import com.happypill.application.pagination.CustomPage;
import com.happypill.application.service.admin.AdminCategoryService;
import com.happypill.application.service.admin.response.AdminCategoryListResponse;
import com.happypill.application.swagger.AuthFailureResponses;
import com.happypill.application.swagger.OKAndServerErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Tag(name = "[관리자] 카테고리", description = "관리자가 카테고리 정보를 조회/관리하기 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private static final String LANGUAGE_HEADER = "Language";

    private final AdminCategoryService adminCategoryService;

    @Operation(summary = "모든 카테고리 조회", description = "카테고리 리스트를 조회합니다.")
    @AuthFailureResponses
    @OKAndServerErrorResponses
    @GetMapping
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminCategoryListResponse> getCategories(@RequestParam(value = "page", defaultValue = "1") int page,
                                                               @RequestParam(value = "size", defaultValue = "5") int size,
                                                               @RequestHeader(LANGUAGE_HEADER) String headerLanguage) {
        Locale locale = Locale.forLanguageTag(headerLanguage);
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminCategoryService.getAllCategories(pageable, locale);
    }
}