package com.happypill.application.repository.product;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.enums.Language;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
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
    List<ProductInfo> getAllProductInfoByCategory(@Param("categoryId") Long categoryId, @Param("createdAt") ZonedDateTime createdAt,
                                                  @Param("language") Language language);

    @Query("""
            SELECT pi
            FROM ProductInfo pi
            JOIN pi.product p
            JOIN p.category c
            WHERE c.id = :categoryId
              AND pi.language = :language
            """)
    List<ProductInfo> getAllProductInfoByCategory(@Param("categoryId") Long categoryId,
                                                  @Param("language") Language language);

    @Query(""" 
            SELECT pi
            FROM ProductInfo pi
            JOIN pi.product p
            WHERE pi.language = :language
                AND p.id = :productId
            """)
    Optional<ProductInfo> getProductInfoByProductId(@Param("productId") Long productId, @Param("language") Language language);

    Optional<Product> findById(Long productId);

    List<Product> findTop8ByOrderByCreatedAt();

    @Query(""" 
            SELECT p
            FROM Product p
            """)
    List<Product> findAllProducts();

    /**
     * for no key update
     * GPT는 ORDER BY 사용을 권장하나, 제외해도 실제로 데드락 발생 X
     **/
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM Product p
            WHERE p.id IN :productIds
            ORDER BY p.id
            """)
    List<Product> findAllByProductIdsWithPessimisticLock(@Param("productIds") List<Long> productIds);
}
