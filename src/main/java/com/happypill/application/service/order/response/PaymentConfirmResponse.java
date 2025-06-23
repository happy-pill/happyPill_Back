package com.happypill.application.service.order.response;

import java.time.ZonedDateTime;

public record PaymentConfirmResponse(
        String orderId,
        String paymentUid,
        String currency,
        int totalPrice,
        ZonedDateTime paidAt,
        String paymentProvider
) {
}
