package com.happypill.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "order_lines")
public class OrderLine extends BaseEntity{

    @Id
    private Long orderLineId;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer month;

    private LocalDate startDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    private boolean isCancelled = false;

    public static OrderLine create(
            Long orderLineId,
            Integer price,
            LocalDate startDate,
            Integer month,
            Product product
    ) {
        return new OrderLine(orderLineId,
                price,
                month,
                startDate,
                null,
                product,
                false);
    }

    public void belongToOrder(Order order) {
        this.order = order;
    }
}
