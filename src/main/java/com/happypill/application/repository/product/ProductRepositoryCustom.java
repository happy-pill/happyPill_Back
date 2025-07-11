package com.happypill.application.repository.product;

import com.happypill.application.entity.enums.Language;
import com.happypill.application.service.product.response.ProductListResponse;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductListResponse> scrollProductsByLanguageAndCategoryWithBestProduct(Long categoryId, Long lastProductId, int size, Language language);
}
