package com.happypill.api.controller;

import com.happypill.application.service.product.ProductService;
import com.happypill.application.service.product.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Tag(name = "상품", description = "상품 정보를 조회하기 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private static final String LANGUAGE_HEADER = "Language";
    private final ProductService productService;

    @Operation(summary = "모든 상품 조회", description = "상품 목록들을 더보기 형식으로 조회합니다.")
    @GetMapping
    public CustomPageResponse<ProductResponse> getProducts(@RequestParam Long categoryId, @RequestParam(required = false) Long lastProductId,
                                                           Locale locale, @RequestParam int size) {
        return productService.getAllProducts(categoryId, lastProductId, locale, size);
    }

    @Operation(summary = "특정 상품 조회", description = "특정 상품을 조회합니다.")
    @GetMapping("/{productId}")
    public ProductInfoResponse getProduct(@PathVariable("productId") Long productId, Locale locale) {
        return productService.getProduct(productId, locale);
    }

    @Operation(summary = "다른 고객이 함께 본 상품 조회", description = "다른 고객이 함께 본 상품을 조회합니다.")
    @GetMapping("/related")
    public List<ProductRelatedResponse> getRecommendation() {
        return productService.getRecommendation();
    }

    @Operation(summary = "모든 Best 상품 조회", description = "Best 상품들을 조회하기 위한 API")
    @GetMapping("/best")
    public List<ProductListResponse> getBestProducts(Locale locale) {
        return productService.getBestProducts(locale);
    }
}