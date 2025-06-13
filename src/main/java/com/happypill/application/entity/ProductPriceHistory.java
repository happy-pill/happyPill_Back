package com.happypill.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Table(name = "product_price_history")
public class ProductPriceHistory extends BaseEntity {

    @Id
    private Long productPriceId;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private boolean isUsed;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static ProductPriceHistory of(Long productPriceId, Integer price, boolean isUsed, Product product) {
        return new ProductPriceHistory(productPriceId, price, isUsed, product);
    }
}
