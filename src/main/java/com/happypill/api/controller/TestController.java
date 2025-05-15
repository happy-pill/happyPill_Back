package com.happypill.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class TestController {

    private static final Instant STARTUP_INSTANT =
            Instant.ofEpochMilli(
                    ManagementFactory.getRuntimeMXBean().getStartTime()
            );

    @GetMapping("/health")
    public void healthCheck() {

    }

    @GetMapping("/")
    public String hello() {
        return "Hello!!!";
    }

    @GetMapping("/started-at")
    public Instant startTime() {
        return STARTUP_INSTANT;
    }

}
