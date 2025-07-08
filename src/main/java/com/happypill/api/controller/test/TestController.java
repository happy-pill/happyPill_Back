package com.happypill.api.controller.test;

import com.happypill.api.config.auth.jwt.JwtService;
import com.happypill.application.event.HappypillEventType;
import com.happypill.application.event.outbox.OutboxEventPublisher;
import com.happypill.application.event.payload.OrderCompletedEventPayload;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final long startupTimestamp = ManagementFactory.getRuntimeMXBean().getStartTime();

    private final HappypillUserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/health")
    public void healthCheck() {

    }

    @GetMapping("/started-at")
    public long startTime() {
        return startupTimestamp;
    }

    @GetMapping("/testToken")
    public String getTestToken() {
        return jwtService.generateAccessToken(1L, new String[]{"USER", "ADMIN"});
    }

    private final OutboxEventPublisher publisher;

    @GetMapping("/tttt")
    @Transactional
    public void tttt() {
        publisher.publish(HappypillEventType.ORDER_COMPLETED,
                OrderCompletedEventPayload.of(
                        1L,
                        ZonedDateTime.now(),
                        List.of(OrderCompletedEventPayload.OrderLinePayload.of(1L, 1, "abc"))
                        , "recipentName",
                        "recipentMobile",
                        "j2234@dev.com"
                )
        );
    }
}
