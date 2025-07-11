package com.happypill.api.config.auth.jwt;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.util.Arrays;

@ConfigurationProperties("jwt")
public record JwtProperties(
        String issuer,
        String secretKey,
        Integer accessTokenTTLSeconds,
        Integer refreshTokenTTLSeconds,
        Duration refreshSessionTtl
) {

    @PostConstruct
    @Profile("dev")
    public void validate() {
        Arrays.stream(JwtProperties.class.getRecordComponents())
                .forEach(rc -> {
                    try {
                        Object value = rc.getAccessor().invoke(this);
                        if (value == null) {
                            throw new IllegalArgumentException("null 설정값: " + rc.getName());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}