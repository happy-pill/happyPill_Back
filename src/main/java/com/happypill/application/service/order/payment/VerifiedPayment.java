package com.happypill.application.service.order.payment;

import java.time.ZonedDateTime;

public record VerifiedPayment(
        String paymentUid,
        long totalAmount,
        String currency,
        ZonedDateTime paidAt,
        String paymentProvider
) {
}
