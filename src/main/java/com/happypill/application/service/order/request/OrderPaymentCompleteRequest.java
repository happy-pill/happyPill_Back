package com.happypill.application.service.order.request;

import jakarta.validation.constraints.NotBlank;

public record OrderPaymentCompleteRequest(
        @NotBlank(message = "결제정보가 필요합니다")
        String paymentUid
) {

}
