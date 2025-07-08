package com.happypill.application.event.outbox;

import com.happypill.application.event.HappypillEvent;
import com.happypill.application.event.HappypillEventPayload;
import com.happypill.application.event.HappypillEventType;
import com.happypill.application.util.SnowflakeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(HappypillEventType type, HappypillEventPayload payload) {
        Outbox outbox = Outbox.create(
                SnowflakeUtil.nextId(),
                type,
                type.getRetryCount(),
                HappypillEvent.of(
                        SnowflakeUtil.nextId(),
                        type, payload
                ).toJson()
        );

        applicationEventPublisher.publishEvent(OutboxEvent.of(outbox));
    }
}
