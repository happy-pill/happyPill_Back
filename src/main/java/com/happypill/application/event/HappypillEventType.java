package com.happypill.application.event;

import com.happypill.application.event.payload.OrderCompletedEventPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@RequiredArgsConstructor
@Slf4j
public enum HappypillEventType {
    ORDER_COMPLETED(OrderCompletedEventPayload.class, 3L);

    private final Class<? extends HappypillEventPayload> paylodClass;

    private final Long retryCount;

    public static HappypillEventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type={}", type, e);
            return null;
        }
    }
}
