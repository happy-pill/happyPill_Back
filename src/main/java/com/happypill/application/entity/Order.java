package com.happypill.application.entity;

import com.happypill.application.entity.enums.OrderStatus;
import com.happypill.application.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity{

    @Id
    private Long orderId;

    private Integer totalPrice;

    @Enumerated(STRING)
    private OrderStatus status;

    @Enumerated(STRING)
    private PaymentMethod paymentMethod;

    private String recipentName;

    private String recipentMobile;

    private String recipentEmail;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private HappypillUser user;

    @OneToMany
    private List<OrderLine> orderLines;
}
