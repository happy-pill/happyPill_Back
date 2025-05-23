package com.happypill.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final long startupTimestamp = ManagementFactory.getRuntimeMXBean().getStartTime();

    @GetMapping("/health")
    public void healthCheck() {

    }

    @GetMapping("/")
    public String hello() {
        return "Hello!!!";
    }

    @GetMapping("/started-at")
    public long startTime() {
        return startupTimestamp;
    }

}
