package com.happypill.application.service.order.payment;

import io.portone.sdk.server.payment.Payment;

public interface PPaymentClient {

    Payment getPayment(String paymentUid);
}


