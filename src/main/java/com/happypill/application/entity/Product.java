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
@Table(name = "products")
public class Product extends BaseEntity{

    @Id
    private Long productId;

    @Column(nullable = false)
    private Integer stock;

    private Integer price;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public static Product of(Long productId, Integer stock, Integer price, boolean isAvailable, String thumbnailUrl, Category category) {
        return new Product(productId, stock, price, isAvailable, thumbnailUrl, false, category);
    }

    public static Product of(Long productId, Integer price, Integer stock, String thumbnailUrl, Category category) {
        return new Product(productId, stock, price, true, thumbnailUrl, false, category);
    }    
}