package com.happypill.api.config.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class JwtService {

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    private static final String REFRESH_TOKEN_SECRET = "secret";
    private final HappypillUserRepository userRepository;
    private final RefreshSessionRepository refreshRepository;


    public JwtService(JwtProperties jwtProperties, RefreshSessionRepository refreshRepository, HappypillUserRepository userRepository) {
        this.jwtProperties = jwtProperties;
        this.algorithm = Algorithm.HMAC256(jwtProperties.secretKey());
        this.jwtVerifier = JWT.require(algorithm).withIssuer(jwtProperties.issuer()).build();

        this.refreshRepository = refreshRepository;
        this.userRepository = userRepository;
    }

    public String issueAccessToken(Long id, String[] roles) {
        return JWT.create()
                .withSubject(String.valueOf(id))
                .withIssuer(jwtProperties.issuer())
                .withIssuedAt(Instant.now())
                .withExpiresAt(calculateExpiration(jwtProperties.accessTokenTTLSeconds()))
                .withArrayClaim("roles", roles)
                .sign(algorithm);
    }

    public String issueRefreshToken(Long id, String[] roles) {
        String secret = UUID.randomUUID().toString();
        String refreshToken = JWT.create()
                .withIssuer(jwtProperties.issuer())
                .withSubject(String.valueOf(id))
                .withIssuedAt(Instant.now())
                .withClaim(REFRESH_TOKEN_SECRET, secret)
                .withExpiresAt(calculateExpiration(jwtProperties.refreshTokenTTLSeconds()))
                .sign(algorithm);

        refreshRepository.save(id, secret, roles, jwtProperties.refreshSessionTtl());
        return refreshToken;
    }


    private Instant calculateExpiration(long ttlSeconds) {
        return Instant.now().plusSeconds(ttlSeconds);
    }


    /**
     * accessToken 검증시에만 사용
     *
     * @param accessToken accessToken
     * @return {@link DecodedJWT}
     */
    public DecodedJWT verifyAndDecode(String accessToken) {
        return jwtVerifier.verify(accessToken);
    }

    /**
     * refreshToken 검증시에만 사용
     *
     * @param refreshToken
     * @return {@link DecodedJWT}
     */
    private DecodedJWT decode(String refreshToken) {
        return JWT.decode(refreshToken);
    }

    public TokenPair rotate(String refreshToken) {
        DecodedJWT decode = decode(refreshToken);
        long userId = Long.parseLong(decode.getSubject());
        String secret = decode.getClaim(REFRESH_TOKEN_SECRET).asString();

        String[] roles;

        if (decode.getExpiresAtAsInstant().isAfter(Instant.now())) {
            roles = refreshRepository.getUserInfo(userId, secret).orElseThrow(() -> new RuntimeException("세션 정보가 존재하지 않습니다."));
            return new TokenPair(issueAccessToken(userId, roles), null);
        }

        // 만료됬을경우 이전 세션을 지우고, 새로운 토큰을 생성한 후 다시 저장.
        roles = refreshRepository.getUserInfoAndDelete(userId, secret).orElseThrow(() -> new RuntimeException("세션 정보가 존재하지 않습니다."));
        return new TokenPair(
                issueAccessToken(userId, roles),
                issueRefreshToken(userId, roles)
        );
    }

    public void delete(String refreshToken) {
        DecodedJWT decode = JWT.decode(refreshToken);
        long userId = Long.parseLong(decode.getSubject());
        String secret = decode.getClaim(REFRESH_TOKEN_SECRET).asString();

        refreshRepository.delete(userId, secret);
    }


    public record TokenPair(String accessToken, String refreshToken) {
    }

}
