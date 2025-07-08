package com.happypill.application.event.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@ToString
@Getter
@AllArgsConstructor(access = PRIVATE)
public class OutboxEvent {
    private Outbox outbox;

    public static OutboxEvent of(Outbox outbox) {
        return new OutboxEvent(outbox);
    }
}
