package com.happypill.api.config.filter;

import com.happypill.api.config.auth.jwt.security.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(request -> true); // requiresAuthentication을 오버라이드하면 동작하지 않는다. 그냥 생성자때문에 넣어놓는것.
        setAuthenticationManager(authenticationManager);
    }

    @Override // 상위 구현체에서, 생성자의 RequestMatcher를 사용하기 때문에, 커스텀하게 오버라이드하는 순간 동작하지 않는다.
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String bearerHeader = request.getHeader(AUTHORIZATION);
        return bearerHeader != null && bearerHeader.startsWith("Bearer ");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("attemptAuthentication - URI: {} | Thread: {}", request.getRequestURI(), Thread.currentThread().getName());
        String accessToken = request.getHeader(AUTHORIZATION).substring(7);
        JwtAuthenticationToken jwtAuthenticationToken = JwtAuthenticationToken.unAuthenticated(accessToken);
        return getAuthenticationManager().authenticate(jwtAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("✅ successfulAuthentication - URI: {} | Thread: {}", request.getRequestURI(), Thread.currentThread().getName());
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("✅ unsuccessfulAuthentication - URI: {} | Thread: {}", request.getRequestURI(), Thread.currentThread().getName());

        super.unsuccessfulAuthentication(request, response, failed);
    }

}
