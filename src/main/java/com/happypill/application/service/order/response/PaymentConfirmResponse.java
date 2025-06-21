package com.happypill.application.service.order.response;

import java.time.ZonedDateTime;

public record PaymentConfirmResponse(
        String orderId,
        String paymentUid,
        String currency,
        Integer totalPrice,
        ZonedDateTime paidAt,
        Object paymentProvider
) {
}
