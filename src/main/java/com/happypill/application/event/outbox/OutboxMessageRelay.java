package com.happypill.application.event.outbox;

import com.happypill.application.event.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxMessageRelay {

    private final OutboxRepository outboxRepository;

    private final MessageSender messageSender;

    private final MessageRelayLockManager lockManager;


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutbox(OutboxEvent outBoxEvent) {
        log.info("outboxEvent: {} ", outBoxEvent);
        outboxRepository.save(outBoxEvent.getOutbox());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(OutboxEvent outboxEvent) {
        log.debug("current_thread = {}", Thread.currentThread());
        log.error(Thread.currentThread().getName());
        _publishEvent(outboxEvent.getOutbox());
    }

    private void _publishEvent(Outbox outbox) {
        log.debug("current_thread = {}", Thread.currentThread());
        try {
            messageSender.sendAsyncMessage(outbox.getPayload())
                    .get(3, TimeUnit.SECONDS);
            outboxRepository.delete(outbox);
        } catch (Exception e) {
            log.error("[OutboxMessageReplay#_publishEvent failed] outbox={} ", outbox, e);
            outboxRepository.decrementRetryCount(outbox.getId());
        }
    }

    @Scheduled(
            fixedDelayString = "${message-relay.fixed-delay-seconds:1}",
            initialDelayString = "${message-relay.initial-delay-seconds:1}",
            timeUnit = TimeUnit.SECONDS,
            scheduler = MessageRelayConfig.MESSAGE_RELAY_PUBLISH_PENDING_EVENT_EXECUTOR
    )
    public void publishPendingEvent() {
        log.debug("current_thread = {}", Thread.currentThread());
        if (!lockManager.getLock()) {
            return;
        }


        try {
            ZonedDateTime oneDayAgo = ZonedDateTime.now().minusDays(1);
            List<Outbox> allRetryableAfter = outboxRepository.findRetryableCreatedAfter(oneDayAgo);

            for (Outbox outbox : allRetryableAfter) {
                _publishEvent(outbox);
            }
        } catch (RuntimeException e) {
            log.error("[OutboxMessageReplay#publishPendingEvent failed]  ", e);
        } finally {
            lockManager.releaseLock();
        }
    }
}
