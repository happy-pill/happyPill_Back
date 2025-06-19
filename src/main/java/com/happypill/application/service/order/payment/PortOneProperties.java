package com.happypill.application.service.order.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("portone")
public record PortOneProperties(
        String apiSecret, String storeId
) {
}
