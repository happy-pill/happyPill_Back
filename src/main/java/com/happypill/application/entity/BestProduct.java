package com.happypill.application.entity;

import com.happypill.application.util.SnowflakeUtil;
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
@Table(name = "best_products")
public class BestProduct extends BaseEntity<Long> {

    @Id
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    public static BestProduct from(Product product) {
        return new BestProduct(SnowflakeUtil.nextId(), product);
    }
}