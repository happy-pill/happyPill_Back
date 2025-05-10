package com.happypill.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/health")
    public void healthCheck() {

    }

    @RequestMapping("/")
    public String hello() {
        return "Hello!!!";
    }

    @RequestMapping("/started-at")
    public Instant startTime() {
        return STARTUP_INSTANT;
    }

}
