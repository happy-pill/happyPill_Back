package com.happypill.api.config.filter;

import com.happypill.api.config.auth.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtService jwtService;
    private final RequestMatcher matcher = new AntPathRequestMatcher("/auth/logout", "POST");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !matcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            ServletResponseUtils.sendErrorResponse(response, HttpStatus.BAD_REQUEST, "Authorization header missing or invalid");
            return;
        }

        String refreshToken = header.substring(BEARER_PREFIX.length());

        jwtService.delete(refreshToken);
    }
}
