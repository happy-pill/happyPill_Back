package com.happypill.application.repository.product;

import com.happypill.application.entity.enums.Language;
import com.happypill.application.service.admin.response.AdminProductResponse;
import com.happypill.application.service.product.response.ProductListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductListResponse> scrollProductsByLanguageAndCategoryWithBestProduct(Long categoryId, Long lastProductId, int size, Language language);

    List<ProductListResponse> findAllBestProductsByLanguage(Language language);

    Page<AdminProductResponse> getAdminProductsByLanguageAndOptionalCategory(Language language, Long categoryId, Pageable pageable);

    Page<AdminProductResponse> searchProductsByKeywordAndLanguage(Pageable pageable, Language language, String keyword);
}

