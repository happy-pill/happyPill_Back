package com.happypill.api.config.auth.oauth2;

import com.happypill.api.config.auth.oauth2.userprovision.OAuth2UserProvisionStrategy;
import com.happypill.application.entity.HappypillUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    //로그인 확장시 사용
    private final OidcUserService oidcDelegate = new OidcUserService();
    private final DefaultOAuth2UserService oauth2Delegate = new DefaultOAuth2UserService();

    private final List<OAuth2UserProvisionStrategy> provisionStrategies;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2User oAuth2User = (userRequest instanceof OidcUserRequest oidcReq)
                ? oidcDelegate.loadUser(oidcReq)
                : oauth2Delegate.loadUser(userRequest);
        try {
            HappypillUser user = provisionStrategies
                    .stream()
                    .filter(st -> st.supports(registrationId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("cannot find paymentProvider"))
                    .provision(oAuth2User);
            return OAuth2UserPrincipal.of(user.getId(), user.getLoginEmail(), List.of(user.getRole()));
        } catch (RuntimeException e) {
            log.error("OAuth2 user provision failed for paymentProvider: {}", registrationId, e);
            throw new OAuth2AuthenticationException(e.getMessage());
        }

    }
}
