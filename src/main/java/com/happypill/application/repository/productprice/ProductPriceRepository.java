package com.happypill.application.repository.productprice;

import com.happypill.application.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    @Query("""
            SELECT pp
            FROM ProductPrice pp
            JOIN pp.product p
            WHERE p.id = :productId
              AND pp.isUsed = true
            """)
    Optional<ProductPrice> getCurrentPriceByProductInfoId(@Param("productId")Long productId);
}
