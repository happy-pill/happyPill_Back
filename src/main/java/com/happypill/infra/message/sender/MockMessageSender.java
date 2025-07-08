package com.happypill.infra.message.sender;

import com.happypill.application.event.HappypillEvent;
import com.happypill.application.event.HappypillEventPayload;
import com.happypill.application.event.MessageSender;
import com.happypill.infra.eventhandler.HappypillEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Component
@Slf4j
@RequiredArgsConstructor
public class MockMessageSender implements MessageSender {

    private final List<HappypillEventHandler> eventHandlerList;

    @Override
    public void sendMessage(String data) {
        log.info("MockMessageSender#sendMessage called. data = {}", data);
    }


    @Override
    public CompletableFuture<Void> sendAsyncMessage(String data) {
        log.info("MockMessageSender#sendAsyncMessage called. data = {}", data);

        HappypillEvent<HappypillEventPayload> event = HappypillEvent.fromJson(data);
        HappypillEventHandler<HappypillEventPayload> eventHandler = findEventHandler(event);

        if (Objects.isNull(eventHandler)) {
            log.error("unsupported event : {}", event);
        } else {
            eventHandler.handle(event);
        }


        try {
            Thread.sleep(1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.completedFuture(null);
    }

    private HappypillEventHandler<HappypillEventPayload> findEventHandler(HappypillEvent<HappypillEventPayload> event) {
        return eventHandlerList
                .stream()
                .filter(h -> h.supports(event))
                .findFirst()
                .orElse(null);
    }
}
