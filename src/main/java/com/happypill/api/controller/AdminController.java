package com.happypill.api.controller;

import com.happypill.application.service.product.response.AdminProductInfoResponse;
import com.happypill.application.service.product.ProductAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final ProductAdminService productAdminService;

    // 상품 관련 엔드포인트
    @GetMapping("/products/{productId}") //특정 상품 조회
    public ResponseEntity<?> getProductDetail(@PathVariable Long productId){
        AdminProductInfoResponse response = productAdminService.getProductDetails(productId);
        return ResponseEntity.ok().body(response);
    }

    // 사용자 관련 엔드포인트

    // 카테고리 관련 엔드포인트
}
