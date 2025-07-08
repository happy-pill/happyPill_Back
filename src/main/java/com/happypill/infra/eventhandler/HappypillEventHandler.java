package com.happypill.infra.eventhandler;

import com.happypill.application.event.HappypillEvent;
import com.happypill.application.event.HappypillEventPayload;

public interface HappypillEventHandler<T extends HappypillEventPayload> {

    void handle(HappypillEvent<T> event);

    boolean supports(HappypillEvent<? extends HappypillEventPayload> event);

}
