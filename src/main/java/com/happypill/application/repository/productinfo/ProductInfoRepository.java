package com.happypill.application.repository.productinfo;

import com.happypill.application.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {

    @Query("SELECT p FROM ProductInfo p WHERE p.product.productId = :productId")
    List<ProductInfo> findProductInfos(@Param("productId") Long productId);
}
