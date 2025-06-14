package com.happypill.application.service.order.response;

import com.happypill.application.entity.OrderRecipientInfo;

public record OrderRecipientInfoResponse(
        String name,
        String mobile,
        String email
) {

    public static OrderRecipientInfoResponse from(OrderRecipientInfo orderRecipientInfo) {
        return new OrderRecipientInfoResponse(orderRecipientInfo.getName(), orderRecipientInfo.getMobile(), orderRecipientInfo.getEmail());
    }

}
