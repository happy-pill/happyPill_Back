package com.happypill.api.controller;

import com.happypill.application.service.product.ProductService;
import com.happypill.application.service.product.dto.response.ProductInfoResponse;
import com.happypill.application.service.product.dto.response.ProductResponse;
import com.happypill.application.service.product.dto.response.CustomPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private static final String LANGUAGE_HEADER = "Language";
    private final ProductService productService;

    @GetMapping
    public CustomPageResponse<ProductResponse> getProducts(@RequestParam Long categoryId, @RequestParam(required = false) Long lastProductId,
                                                           @RequestHeader(LANGUAGE_HEADER) String headerLanguage, @RequestParam int size) {
        Locale locale = Locale.of(headerLanguage);
        return productService.getAllProducts(categoryId, lastProductId, locale, size);
    }

    @GetMapping("/{productId}")
    public ProductInfoResponse getProduct(@PathVariable("productId") Long productId, @RequestHeader(LANGUAGE_HEADER) String headerLanguage) {
        Locale locale = Locale.of(headerLanguage);
        return productService.getProduct(productId, locale);
    }
}
