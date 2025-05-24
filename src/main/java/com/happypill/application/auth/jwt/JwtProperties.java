package com.happypill.application.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
@Getter
@Setter
public class JwtProperties {
    private String issuer;
    private String secretKey;
    private Integer accessTokenTTLSeconds;
    private Integer refreshTokenTTLSeconds;
}