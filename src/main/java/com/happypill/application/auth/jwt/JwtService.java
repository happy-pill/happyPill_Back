package com.happypill.application.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;

    public String generateAccessToken(@NonNull Long id, String[] roles) {
        return JWT.create()
                .withSubject(String.valueOf(id))
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(Instant.now())
                .withExpiresAt(calculateExpiration(jwtProperties.getAccessTokenTTLSeconds()))
                .withArrayClaim("roles", roles)
                .sign(algorithm);
    }


    private Instant calculateExpiration(long ttlSeconds) {
        return Instant.now().plusSeconds(ttlSeconds);
    }

}
