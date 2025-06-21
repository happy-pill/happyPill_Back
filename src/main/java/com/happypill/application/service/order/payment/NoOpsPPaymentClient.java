package com.happypill.application.service.order.payment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoOpsPPaymentClient implements PPaymentClient {
    @Override
    public VerifiedPayment getVerifiedPayment(String paymentUid) {
        log.info("paymentUid = {}", paymentUid);
        return null;
    }
}
