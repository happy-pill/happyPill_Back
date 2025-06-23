package com.happypill.application.service.order.payment;

import io.portone.sdk.server.payment.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class PortonePPaymentClient implements PPaymentClient {

    private final PaymentClient portone;

    @Autowired
    public PortonePPaymentClient(PortOneProperties properties) {
        this.portone = new PaymentClient(properties.apiSecret(), "https://api.portone.io", properties.storeId());
    }

    public VerifiedPayment getVerifiedPayment(String paymentUid) {
        try {
            Payment rawPayment = portone.getPayment(paymentUid)
                    .get();
            if (rawPayment instanceof PaidPayment paidPayment) {
                return new VerifiedPayment(
                        paidPayment.getId(),
                        paidPayment.getAmount().getTotal(),
                        paidPayment.getCurrency().getValue(),
                        paidPayment.getPaidAt().atZone(ZoneId.systemDefault()),
                        getPaymentProvider(paidPayment.getMethod())
                );
            } else {
                throw new RuntimeException("유효한 결제정보가 아닙니다");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getPaymentProvider(PaymentMethod method) {
        try {
            return switch (method) {
                case PaymentMethodEasyPay easyPay -> easyPay.getProvider().getValue();
                case PaymentMethodCard card -> card.getCard().getName();
                default -> "unknown";
            };
        } catch (NullPointerException e) {
            return "unknown";
        }
    }
}
