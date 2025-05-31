package com.happypill.api.config.auth.jwt.security;

import com.happypill.api.config.auth.usercontext.SecurityUserContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String accessToken;

    private final SecurityUserContext principal;

    private JwtAuthenticationToken(SecurityUserContext principal, String accessToken, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.accessToken = accessToken;
        this.principal = principal;
        this.setAuthenticated(true);
    }

    private JwtAuthenticationToken(SecurityUserContext principal, String accessToken) {
        super(null);
        this.principal = principal;
        this.accessToken = accessToken;
        setAuthenticated(false);
    }

    public static JwtAuthenticationToken unAuthenticated(String jwtToken) {
        return new JwtAuthenticationToken(null, jwtToken);
    }

    public static JwtAuthenticationToken authenticated(SecurityUserContext principal, String jwtToken, Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(principal, jwtToken, authorities);
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
