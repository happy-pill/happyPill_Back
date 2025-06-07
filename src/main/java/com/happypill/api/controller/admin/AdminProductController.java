package com.happypill.api.controller.admin;

import com.happypill.application.pagination.CustomPage;
import com.happypill.application.service.admin.response.AdminProductInfoResponse;
import com.happypill.application.service.admin.AdminProductService;
import com.happypill.application.service.admin.response.AdminProductListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private static final String LANGUAGE_HEADER = "Language";

    private final AdminProductService adminProductService;

    //모든 상품 조회
    @GetMapping
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminProductListResponse> getProducts(@RequestParam(value = "categories", required = false) Long categoryId,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "size", defaultValue = "8") int size,
                                                            @RequestHeader(LANGUAGE_HEADER) String headerLanguage) {
        Locale locale = Locale.forLanguageTag(headerLanguage);
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminProductService.getAllProducts(categoryId, pageable, locale);
    }

    //특정 상품 조회
    @GetMapping("/{productId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public AdminProductInfoResponse getProductDetail(@PathVariable Long productId){
       return adminProductService.getProductDetails(productId);
    }
}