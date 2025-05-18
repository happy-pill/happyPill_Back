package com.happypill.application.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "OrderLines")
public class OrderLine extends BaseEntity{

    @Id
    @Column(name = "order_line_id")
    private Long orderLineId;

    private Integer price;

    private Integer month;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany
    private List<Subscription> subscriptions;
}
