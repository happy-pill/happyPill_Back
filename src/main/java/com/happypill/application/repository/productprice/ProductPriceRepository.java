package com.happypill.application.repository.productprice;

import com.happypill.application.entity.ProductPriceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPriceHistory, Long> {
    @Query("""
            SELECT pp
            FROM ProductPriceHistory pp
            JOIN pp.product p
            WHERE p.productId = :productId
              AND pp.isUsed = true
            """)
    Optional<ProductPriceHistory> getCurrentPriceByProductId(@Param("productId") Long productId);

    @Query("SELECT p FROM ProductPriceHistory p WHERE p.product.productId = :productId AND p.isUsed = true")
    Optional<ProductPriceHistory> findCurrentPriceByProduct(@Param("productId") Long productId);

    @Query("""
            SELECT pp
            FROM ProductPriceHistory pp
            JOIN pp.product p ON pp.product.productId = p.productId
            WHERE p.productId = :productId
            ORDER BY pp.createdAt DESC
            """)
    Page<ProductPriceHistory> getCurrentPriceByProductId(@Param("productId") Long productId, Pageable pageable);
}
