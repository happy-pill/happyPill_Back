package com.happypill.api.controller.test;

import com.happypill.api.config.auth.jwt.JwtService;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;

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

}
