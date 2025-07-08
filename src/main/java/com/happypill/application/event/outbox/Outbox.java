package com.happypill.application.event.outbox;

import com.happypill.application.event.HappypillEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "outbox")
@Entity
@ToString
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class Outbox {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private HappypillEventType eventType;

    @Column(nullable = false, length = 5000)
    private String payload;

    @Column(nullable = false)
    private Long retryCount;

    private ZonedDateTime createdAt;

    public static Outbox create(Long outboxId, HappypillEventType eventType, Long retryCount, String payload) {
        return new Outbox(outboxId, eventType, payload, retryCount, ZonedDateTime.now());
    }
}
