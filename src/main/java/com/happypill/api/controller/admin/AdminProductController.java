package com.happypill.api.controller.admin;

import com.happypill.application.service.admin.response.AdminProductInfoResponse;
import com.happypill.application.service.admin.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping("/{productId}") //특정 상품 조회
    public AdminProductInfoResponse getProductDetail(@PathVariable Long productId){
       return adminProductService.getProductDetails(productId);
    }
}