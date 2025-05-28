package com.happypill.api.config.auth.oauth2;

import com.happypill.application.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class OAuth2UserPrincipal implements OidcUser, OAuth2User {

    private final Long id;
    private final String email;
    private final List<Role> roles;
    private final Map<String, Object> attributes;
    private final Map<String, Object> claims;

    public static OAuth2UserPrincipal of(Long id, String email, List<Role> roles) {
        return new OAuth2UserPrincipal(id, email, roles, null, null);
    }

    public String[] getRoleNames() {
        return roles.stream()
                .map(Role::name)
                .toArray(String[]::new);
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.name()))
                .toList();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}
