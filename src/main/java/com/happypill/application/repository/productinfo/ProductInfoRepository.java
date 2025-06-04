package com.happypill.application.repository.productinfo;

import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.service.admin.response.AdminProductListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {

    @Query("SELECT p FROM ProductInfo p WHERE p.product.productId = :productId")
    List<ProductInfo> findAllByProductId(@Param("productId") Long productId);

    @Query("""
    SELECT pi
    FROM ProductInfo pi
    JOIN pi.product p
    JOIN ProductPrice pp ON pp.product = p AND pp.isUsed = true
    WHERE pi.language = :language
    ORDER BY p.productId DESC
""")
    Page<ProductInfo> getAllProductInfosByLanguage(@Param("language") Language language, Pageable pageable);

    @Query("""
    SELECT pi
    FROM ProductInfo pi
    JOIN pi.product p
    JOIN ProductPrice pp ON pp.product = p AND pp.isUsed = true
    WHERE pi.language = :language
    AND p.category.categoryId = :categoryId
    ORDER BY p.productId DESC
""")
    Page<ProductInfo> getAllProductInfosByCategoryAndLanguage(@Param("categoryId") Long categoryId, @Param("language") Language language, Pageable pageable);
}