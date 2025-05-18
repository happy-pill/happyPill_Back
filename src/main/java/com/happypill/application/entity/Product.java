package com.happypill.application.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Products")
public class Product extends BaseEntity{

    @Id
    @Column(name = "product_id")
    private Long productId;

    private Integer stock;

    @Column(name = "is_available")
    private Boolean isAvailable;

    private String thumbnail;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany
    private List<ProductInfo> productInfoList;

    @OneToMany
    private List<OrderLine> orderLines;

    @OneToMany
    private List<ProductPrice> productPrices;
}
