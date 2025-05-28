package com.happypill.api.config.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JwtProperties(
        String issuer,
        String secretKey,
        Integer accessTokenTTLSeconds,
        Integer refreshTokenTTLSeconds
) {
}