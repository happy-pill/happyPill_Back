package com.happypill.application.repository.product;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.service.admin.response.AdminProductListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductId(Long productId);

    @Query("""
    SELECT new com.happypill.application.service.admin.response.AdminProductListResponse(
        p.productId,
        p.category.categoryId,
        pi.name,
        pi.company,
        pp.price,
        pi.briefDescription,
        p.thumbnailUrl,
        p.isAvailable
    )
    FROM ProductInfo pi
    JOIN pi.product p
    JOIN ProductPrice pp ON pp.product = p AND pp.isUsed = true
    WHERE pi.language = :language
    ORDER BY p.productId DESC
""")
    Page<AdminProductListResponse> getAllProductInfosByLanguage(@Param("language") Language language, Pageable pageable);

    @Query("""
    SELECT new com.happypill.application.service.admin.response.AdminProductListResponse(
        p.productId,
        p.category.categoryId,
        pi.name,
        pi.company,
        pp.price,
        pi.briefDescription,
        p.thumbnailUrl,
        p.isAvailable
    )
    FROM ProductInfo pi
    JOIN pi.product p
    JOIN ProductPrice pp ON pp.product = p AND pp.isUsed = true
    WHERE pi.language = :language
    AND p.category.categoryId = :categoryId
    ORDER BY p.productId DESC
""")
    Page<AdminProductListResponse> getAllProductInfosByCategoryAndLanguage(@Param("categoryId") Long categoryId, @Param("language") Language language, Pageable pageable);

}
