package com.happypill.application.service.order.response;

import com.happypill.application.entity.Order;
import com.happypill.application.entity.enums.OrderStatus;
import com.happypill.application.entity.enums.PaymentMethod;

import java.time.ZonedDateTime;
import java.util.List;

public record OrderResponse(
        String orderId, //Long
        Integer totalPrice,
        String paymentUid,
        OrderStatus status,
        PaymentMethod paymentMethod,
        String userId,

        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,

        OrderRecipientInfoResponse recipientInfo,
        List<OrderLineResponse> orderLines
) {

    public static OrderResponse from(Order order) {
        List<OrderLineResponse> orderLines = order.getOrderLines().stream()
                .map(OrderLineResponse::from)
                .toList();
        return new OrderResponse(
                String.valueOf(order.getOrderId()),
                order.getTotalPrice(),
                order.getPaymentUid(),
                order.getStatus(),
                order.getPaymentMethod(),
                String.valueOf(order.getUser().getUserId()),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                com.happypill.application.service.order.response.OrderRecipientInfoResponse.from(order.getOrderRecipientInfo()),
                orderLines
        );

    }

}
