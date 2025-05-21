package com.happypill.application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "subscriptions")
public class Subscription extends BaseEntity{

    @Id
    private Long subscriptionId;

    @Column(name = "expired_date", columnDefinition = "TIMESTAMPTZ")
    private ZonedDateTime expiredDate;

    private boolean isCompleted;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_line_id", nullable = false)
    private OrderLine orderLine;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private HappypillUser user;
}
