package com.happypill.application.service;

import com.happypill.application.client.EmailSender;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public abstract class IntegrationTestSupport {

    //
    protected static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:8")
            .withExposedPorts(6379)
            .withReuse(true);

    static {
        redisContainer.start();
    }


    @DynamicPropertySource
    static void configureRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @MockitoBean
    protected EmailSender emailSender;

}
