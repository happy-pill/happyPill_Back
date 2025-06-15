package com.happypill.application.entity;

import com.happypill.application.entity.enums.OrderStatus;
import com.happypill.application.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Table(name = "orders")
public class Order extends BaseEntity{

    @Id
    private Long orderId;

    @Column(nullable = false)
    private Integer totalPrice;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false, length = 50)
    private String recipentName;

    @Column(nullable = false)
    private String recipentMobile;

    @Column(nullable = false)
    private String recipentEmail;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private HappypillUser user;

    @OneToMany(mappedBy = "order", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    public static Order of(Long orderId, Integer totalPrice, OrderStatus status, PaymentMethod paymentMethod, String recipentName,
                           String recipentMobile, String recipentEmail, HappypillUser user){
        return new Order(orderId, totalPrice, status, paymentMethod, recipentName, recipentMobile, recipentEmail, user, new ArrayList<>());
    }
}
