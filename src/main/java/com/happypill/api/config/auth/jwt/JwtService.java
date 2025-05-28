package com.happypill.api.config.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtService {

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.algorithm = Algorithm.HMAC256(jwtProperties.secretKey());
        this.jwtVerifier = JWT.require(algorithm).withIssuer(jwtProperties.issuer()).build();
    }

    public String generateAccessToken(Long id, String[] roles) {
        return JWT.create()
                .withSubject(String.valueOf(id))
                .withIssuer(jwtProperties.issuer())
                .withIssuedAt(Instant.now())
                .withExpiresAt(calculateExpiration(jwtProperties.accessTokenTTLSeconds()))
                .withArrayClaim("roles", roles)
                .sign(algorithm);
    }

    public DecodedJWT parse(String token) {
        return jwtVerifier.verify(token);
    }

    private Instant calculateExpiration(long ttlSeconds) {
        return Instant.now().plusSeconds(ttlSeconds);
    }

}
