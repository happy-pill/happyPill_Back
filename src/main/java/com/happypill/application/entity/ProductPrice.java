package com.happypill.application.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ProductPrices")
public class ProductPrice extends BaseEntity{

    @Id
    @Column(name = "product_price_id")
    private Long productPriceId;

    private Integer price;

    @Column(name = "is_used")
    private Boolean isUsed;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
