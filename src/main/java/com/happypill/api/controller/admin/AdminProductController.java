package com.happypill.api.controller.admin;

import com.happypill.application.pagination.CustomPage;
import com.happypill.application.service.admin.request.AdminProductCreateRequest;
import com.happypill.application.service.admin.request.AdminProductUpdateRequest;
import com.happypill.application.service.admin.response.AdminProductInfoResponse;
import com.happypill.application.service.admin.AdminProductService;
import com.happypill.application.service.admin.response.AdminProductListResponse;
import com.happypill.application.service.admin.response.AdminProductPriceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    //금액 기록 조회
    @GetMapping("/{productId}/price-history")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminProductPriceResponse> getProductPriceHistory(@PathVariable(value = "productId") Long productId,
                                                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                                                        @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminProductService.getAllProductPrices(productId, pageable);
    }

    //상품 등록
    @PostMapping
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody AdminProductCreateRequest request) {
        long productId = adminProductService.createProduct(request);
        return ResponseEntity.created(URI.create("/api/admin/products/" + productId)).build();
    }

    //상품 수정
    @PatchMapping("/{productId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public AdminProductInfoResponse updateProduct(@PathVariable("productId") Long productId,
                                                  @Valid @RequestBody AdminProductUpdateRequest request) {
        return adminProductService.updateProduct(productId, request);
    }
}