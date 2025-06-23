package com.happypill.application.service.admin.response;

import java.time.ZonedDateTime;

public record AdminSubscriptionListResponse(
        String productName,
        String notifyEmail,
        String subscriptionId,
        ZonedDateTime nextDeliveryDate
) {
    public static AdminSubscriptionListResponse fromArray(Object[] array){
        String productName = (String) array[0];
        String notifyEmail = (String) array[1];
        String subscriptionId = String.valueOf(array[2]);
        ZonedDateTime nextDeliveryDate = (ZonedDateTime) array[3];

        return new AdminSubscriptionListResponse(
                productName,
                notifyEmail,
                subscriptionId,
                nextDeliveryDate
        );
    }
}