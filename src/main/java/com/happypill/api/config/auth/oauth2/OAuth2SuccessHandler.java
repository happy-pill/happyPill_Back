package com.happypill.api.config.auth.oauth2;

import com.happypill.api.config.auth.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();

        String accessToken = jwtService.issueAccessToken(principal.getId(), principal.getRoleNames());
        String refreshToken = jwtService.issueRefreshToken(principal.getId(), principal.getRoleNames());

        //TODO refreshToken 전략
        String redirectUrl = UriComponentsBuilder
                .fromPath("/oauth2-test")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
