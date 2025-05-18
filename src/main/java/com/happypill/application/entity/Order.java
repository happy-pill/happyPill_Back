package com.happypill.application.entity;

import com.happypill.application.entity.enums.OrderStatus;
import com.happypill.application.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Orders")
public class Order extends BaseEntity{

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "receipent_name")
    private String receipentName;

    @Column(name = "receipent_mobile")
    private Integer receipentMobile;

    @Column(name = "receipent_email")
    private String receipentEmail;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany
    private List<OrderLine> orderLines;
}
