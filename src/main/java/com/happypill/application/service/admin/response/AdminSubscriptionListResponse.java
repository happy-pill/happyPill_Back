package com.happypill.application.service.admin.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Setter
@Getter
public class AdminSubscriptionListResponse {
    private String productName;
    private String notifyEmail;
    private String subscriptionId;
    private ZonedDateTime nextDeliveryDate;

    @QueryProjection
    public AdminSubscriptionListResponse(
            String productName,
            String notifyEmail,
            String subscriptionId,
            ZonedDateTime nextDeliveryDate
    ) {
        this.productName = productName;
        this.notifyEmail = notifyEmail;
        this.subscriptionId = subscriptionId;
        this.nextDeliveryDate = nextDeliveryDate;
    }
}
