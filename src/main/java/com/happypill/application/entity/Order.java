package com.happypill.application.entity;

import com.happypill.application.entity.enums.OrderStatus;
import com.happypill.application.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity<Long> {

    @Id
    private Long id;

    @Column(nullable = false)
    private Integer totalPrice = 0;

    private String paymentUid;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Embedded
    private OrderRecipientInfo orderRecipientInfo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private HappypillUser user;

    @OneToMany(mappedBy = "order", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    public void complete() {
        if (this.status == OrderStatus.PENDING) {
            this.status = OrderStatus.COMPLETED;
        } else {
            throw new RuntimeException(String.format("Cannot confirm  payment, OrderStatus = %s", this.status));
        }
    }

    public static Order create(
            Long orderId,
            String paymentUid,
            PaymentMethod paymentMethod,
            OrderRecipientInfo orderRecipientInfo,
            HappypillUser user,
            List<OrderLine> orderLines
    ) {
        Order o = new Order();
        o.id = orderId;
        o.paymentUid = paymentUid;
        o.status = OrderStatus.PENDING;
        o.paymentMethod = paymentMethod;
        o.orderRecipientInfo = orderRecipientInfo;
        o.user = user;

        for (OrderLine ol : orderLines) {
            o.addOrderLine(ol);
        }
        return o;
    }

    public void addOrderLine(OrderLine orderLine) {
        this.orderLines.add(orderLine);
        this.totalPrice += orderLine.getPrice();
        orderLine.belongToOrder(this);
    }


}
