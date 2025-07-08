package com.happypill.application.event;

import com.happypill.application.util.DataSerializerUtil;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HappypillEvent<T extends HappypillEventPayload> {
    private Long eventId;
    private HappypillEventType type;
    private T payload;

    public static HappypillEvent<HappypillEventPayload> of(Long eventId, HappypillEventType type, HappypillEventPayload payload) {
        HappypillEvent<HappypillEventPayload> happypillEvent = new HappypillEvent<>();
        happypillEvent.eventId = eventId;
        happypillEvent.type = type;
        happypillEvent.payload = payload;
        return happypillEvent;
    }

    public static HappypillEvent<HappypillEventPayload> fromJson(String json) {
        EventRaw eventRaw = DataSerializerUtil.deserialize(json, EventRaw.class);

        HappypillEvent<HappypillEventPayload> event = new HappypillEvent<>();
        event.eventId = eventRaw.getEventId();
        event.type = HappypillEventType.from(eventRaw.getType());
        event.payload = DataSerializerUtil.deserialize(eventRaw.getPayload(), event.type.getPaylodClass());
        return event;
    }

    public String toJson() {
        return DataSerializerUtil.serialize(this);
    }

    @Getter
    private static class EventRaw {
        private Long eventId;
        private String type;
        private Object payload;
    }


}
