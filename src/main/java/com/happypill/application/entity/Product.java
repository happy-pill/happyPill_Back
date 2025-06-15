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

    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public static Product of(Long productId, Integer price, Integer stock, boolean isAvailable, String thumbnailUrl, boolean isDeleted, Category category) {
        return new Product(productId, price, stock, isAvailable, thumbnailUrl, isDeleted, category);
    }

    public static Product of(Long productId, Integer price, Integer stock, String thumbnailUrl, Category category) {
        return new Product(productId, price, stock, true, thumbnailUrl, false, category);
    }

    public void update(Integer stock, boolean isAvailable, String thumbnailUrl, Category category) {
        this.stock = stock;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
        this.isAvailable = stock != 0 && isAvailable;
    }

    public void deleteProduct(){
        this.isDeleted = true;
        this.isAvailable = false;
    }
}