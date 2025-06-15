package com.happypill.api.controller;

import com.happypill.application.exception.global.ErrorResponse;
import com.happypill.application.service.product.ProductService;
import com.happypill.application.service.product.dto.response.CustomPageResponse;
import com.happypill.application.service.product.dto.response.ProductInfoResponse;
import com.happypill.application.service.product.dto.response.ProductResponse;
import com.happypill.application.service.product.response.ProductRelatedResponse;
import com.happypill.application.swagger.OKAndServerErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @OKAndServerErrorResponses
    @ApiResponse(responseCode = "404", description = "상픔정보가 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public CustomPageResponse<ProductResponse> getProducts(@RequestParam Long categoryId, @RequestParam(required = false) Long lastProductId,
                                                           @RequestHeader(LANGUAGE_HEADER) String headerLanguage, @RequestParam int size) {
        Locale locale = Locale.of(headerLanguage);
        return productService.getAllProducts(categoryId, lastProductId, locale, size);
    }

    @Operation(summary = "특정 상품 조회", description = "특정 상품을 조회합니다.")
    @OKAndServerErrorResponses
    @ApiResponse(responseCode = "404", description = "상픔정보가 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{productId}")
    public ProductInfoResponse getProduct(@PathVariable("productId") Long productId, @RequestHeader(LANGUAGE_HEADER) String headerLanguage) {
        Locale locale = Locale.of(headerLanguage);
        return productService.getProduct(productId, locale);
    }

    @GetMapping("/related")
    public List<ProductRelatedResponse> getRecommendation() {
        return productService.getRecommendation();
    }
}