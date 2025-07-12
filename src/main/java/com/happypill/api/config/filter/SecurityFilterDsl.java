package com.happypill.api.config.filter;

import com.happypill.api.config.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityFilterDsl extends AbstractHttpConfigurer<SecurityFilterDsl, HttpSecurity> {

    private final JwtService jwtService;

    @Override
    public void configure(HttpSecurity builder) {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

        JwtLogoutFilter jwtLogoutFilter = new JwtLogoutFilter(jwtService);
        builder.addFilterBefore(jwtLogoutFilter, UsernamePasswordAuthenticationFilter.class);

        JwtRotateFilter jwtRotateFilter = new JwtRotateFilter(jwtService);
        builder.addFilterAfter(jwtRotateFilter, JwtLogoutFilter.class);

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
        builder.addFilterAfter(jwtAuthenticationFilter, JwtRotateFilter.class);


    }

}
