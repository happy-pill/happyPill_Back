package com.happypill.api.controller.test;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 개발용 임시 redirect
 */
@Hidden
@RestController
public class OAuth2TestController {

    /**
     * Vercel 등으로 배포된 프로덕션 프론트엔드 URL
     */
    @Value("${app.frontend.base-url}")
    private String baseUrl;

    /**
     * 프론트엔드 로그인 후 진입 경로 (예: "/login/callback")
     */
    @Value("${app.frontend.login-path:}")
    private String loginPath;

    /**
     * 예: GET /?accessToken=...&refreshToken=...
     */
    @GetMapping(value = "/oauth2-test", produces = MediaType.TEXT_HTML_VALUE)
    public String renderLinks(
            @RequestParam String accessToken,
            @RequestParam String refreshToken) {

        // 로컬 프론트엔드 URL
        String localUrl = "http://localhost:5173" + loginPath
                + "?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken;

        // 프로덕션 프론트엔드 URL
        String prodUrl = baseUrl + loginPath
                + "?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken;

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8"/>
                    <title>OAuth2 Debug Callback</title>
                </head>
                <body>
                    <h1>디버깅용 OAuth2 콜백 페이지</h1>
                    <p>accessToken: %s</p>
                    <p>refreshToken: %s</p>
                    <p><a href="%s">로컬 프론트엔드로 이동</a></p>
                    <p><a href="%s">프로덕션 프론트엔드로 이동</a></p>
                </body>
                </html>
                """.formatted(
                accessToken,
                refreshToken,
                localUrl,
                prodUrl
        );
    }
}
