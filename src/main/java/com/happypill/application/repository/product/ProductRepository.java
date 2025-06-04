package com.happypill.application.repository.product;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
            SELECT pi
            FROM ProductInfo pi
            JOIN pi.product p
            JOIN p.category c
            WHERE c.id = :categoryId
              AND pi.language = :language
              AND p.createdAt > :createdAt
            """)
    List<ProductInfo> getAllProductInfoByCategory(@Param("categoryId")Long categoryId, @Param("createdAt")ZonedDateTime createdAt,
                                               @Param("language") Language language);

    @Query("""
            SELECT pi
            FROM ProductInfo pi
            JOIN pi.product p
            JOIN p.category c
            WHERE c.id = :categoryId
              AND pi.language = :language
            """)
    List<ProductInfo> getAllProductInfoByCategory(@Param("categoryId")Long categoryId,
                                               @Param("language") Language language);

    @Query(""" 
            SELECT pi
            FROM ProductInfo pi
            JOIN pi.product p
            WHERE pi.language = :language
                AND p.productId = :productId
            """)
    Optional<ProductInfo> getProductInfoByProductId(@Param("productId")Long productId, @Param("language") Language language);
    
    Optional<Product> findByProductId(Long productId);
}
