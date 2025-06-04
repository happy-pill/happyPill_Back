package com.happypill.api.config.auth.jwt.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.happypill.api.config.auth.jwt.JwtService;
import com.happypill.api.config.auth.usercontext.SecurityUserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {

            String accessToken = (String) authentication.getCredentials();
            DecodedJWT parsedJwt = jwtService.parse(accessToken);

            String subject = parsedJwt.getSubject();
            Long userId = Long.valueOf(subject);

            String[] roles = parsedJwt.getClaim("roles").asArray(String.class);
            for (int i = 0; i < roles.length; i++) {
                roles[i] = "ROLE_" + roles[i];
            }

            List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(roles);

            return JwtAuthenticationToken.authenticated(SecurityUserContext.from(userId), accessToken, authorityList);
        } catch (JWTVerificationException | NumberFormatException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
