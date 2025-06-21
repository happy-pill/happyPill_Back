package com.happypill.application.service.order.payment;

import io.portone.sdk.server.payment.Payment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoOpsPPaymentClient implements PPaymentClient {
    @Override
    public Payment getPayment(String paymentUid) {
        log.info("paymentUid = {}", paymentUid);
        return null;
    }
}
