package com.happypill.application.service;

import com.happypill.application.email.EmailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;

public abstract class IntegrationTestSupport {

    protected static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:8")
            .withExposedPorts(6379);

    static {
        redisContainer.start();
        System.setProperty("spring.redis.host", redisContainer.getHost());
        System.setProperty("spring.redis.port", String.valueOf(redisContainer.getMappedPort(6379)));
    }

    @MockitoBean
    protected EmailSender emailSender;


}
