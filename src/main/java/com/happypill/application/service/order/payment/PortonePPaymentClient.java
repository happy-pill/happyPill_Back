package com.happypill.application.service.order.payment;

import io.portone.sdk.server.payment.Payment;
import io.portone.sdk.server.payment.PaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PortonePPaymentClient implements PPaymentClient {

    private final PaymentClient portone;

    @Autowired
    public PortonePPaymentClient(PortOneProperties properties) {
        this.portone = new PaymentClient(properties.apiSecret(), "https://api.portone.io", properties.storeId());
    }

    public Payment getPayment(String paymentUid) {
        try {
            return portone.getPayment(paymentUid)
                    .get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
