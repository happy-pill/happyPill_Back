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
@Table(name = "order_lines")
public class OrderLine extends BaseEntity{

    @Id
    private Long orderLineId;

    private Integer price;

    private Integer month;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static OrderLine of(Long orderLineId, Integer price, Integer month, Order order, Product product){
        return new OrderLine(orderLineId, price, month, order, product);
    }
}
