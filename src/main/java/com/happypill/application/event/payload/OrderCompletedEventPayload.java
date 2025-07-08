package com.happypill.application.event.payload;

import com.happypill.application.event.HappypillEventPayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@ToString
public class OrderCompletedEventPayload implements HappypillEventPayload {
    private Long orderId;
    private ZonedDateTime createdAt;

    private List<OrderLinePayload> orderLines;

    private String recipentName;
    private String recipentMobile;
    private String recipentEmail;

    public static OrderCompletedEventPayload of(Long orderId, ZonedDateTime createdAt,
                                                List<OrderLinePayload> orderLines, String recipentName,
                                                String recipentMobile, String recipentEmail) {
        return new OrderCompletedEventPayload(orderId, createdAt, orderLines,
                recipentName, recipentMobile, recipentEmail);
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = PRIVATE)
    @ToString
    public static class OrderLinePayload {
        private Long orderLineId;
        private Integer month;
        private String productName;

        public static OrderLinePayload of(Long orderLineId, Integer month, String productName) {
            return new OrderLinePayload(orderLineId, month, productName);
        }
    }

}
