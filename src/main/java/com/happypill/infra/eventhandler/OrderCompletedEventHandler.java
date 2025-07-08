package com.happypill.infra.eventhandler;

import com.happypill.application.event.HappypillEvent;
import com.happypill.application.event.HappypillEventType;
import com.happypill.application.event.payload.OrderCompletedEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCompletedEventHandler implements HappypillEventHandler<OrderCompletedEventPayload> {
    @Override
    public void handle(HappypillEvent<OrderCompletedEventPayload> event) {
        log.info("event = {}", event);
    }

    @Override
    public boolean supports(HappypillEvent<?> event) {
        return event.getType().equals(HappypillEventType.ORDER_COMPLETED);
    }
}
