package com.happypill.application.service.order.response;

import com.happypill.application.entity.OrderLine;

public record OrderLineResponse(
        String orderLineId, //Long
        Integer price,
        Integer month,
        String orderId,
        String productId, //Long
        boolean isCancelled
) {

    public static OrderLineResponse from(OrderLine orderLine) {
        return new OrderLineResponse(
                String.valueOf(orderLine.getOrderLineId()),
                orderLine.getPrice(),
                orderLine.getMonth(),
                String.valueOf(orderLine.getOrder().getOrderId()),
                String.valueOf(orderLine.getProduct().getProductId()),
                orderLine.isCancelled()
        );
    }
}
