package com.happypill.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Table(name = "subscriptions")
public class Subscription extends BaseEntity<Long> {

    @Id
    private Long id;

    @Column(name = "expired_date", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private ZonedDateTime expiredDate;

    @Column(name = "next_delivery_date", columnDefinition = "TIMESTAMPTZ")
    private ZonedDateTime nextDeliveryDate;

    @Column(nullable = false)
    private boolean isCompleted;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_line_id", nullable = false)
    private OrderLine orderLine;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private HappypillUser user;

    public static Subscription of(Long id,
                                   ZonedDateTime expiredDate,
                                   ZonedDateTime nextDeliveryDate,
                                   boolean isCompleted,
                                   OrderLine orderLine,
                                   HappypillUser user){
        return new Subscription(id, expiredDate, nextDeliveryDate, isCompleted, orderLine, user);
    }
}
