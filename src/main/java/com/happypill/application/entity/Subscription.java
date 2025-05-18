package com.happypill.application.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Subscriptions")
public class Subscription extends BaseEntity{

    @Id
    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "expired_date", columnDefinition = "TIMESTAMPTZ")
    private Date expiredDate;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "order_line_id", nullable = false)
    private OrderLine orderLine;
}
