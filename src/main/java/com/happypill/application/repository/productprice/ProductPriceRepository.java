package com.happypill.application.repository.productprice;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    @Query("""
            SELECT pp
            FROM ProductPrice pp
            JOIN pp.product p
            WHERE p.productId = :productId
              AND pp.isUsed = true
            """)
    Optional<ProductPrice> getCurrentPriceByProductInfoId(@Param("productId")Long productId);
    
    @Query("SELECT p FROM ProductPrice p WHERE p.product.productId = :productId AND p.isUsed = true")
    Optional<ProductPrice> findCurrentPriceByProduct(@Param("productId") Long productId);
}
