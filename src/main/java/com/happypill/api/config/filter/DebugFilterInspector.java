package com.happypill.api.config.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 테스트 목적으로 사용할 컴포넌트
 * 개발기간 끝나면 제거 예정
 */
@Profile("dev")
@Component
@RequiredArgsConstructor
public class DebugFilterInspector implements ApplicationRunner {
    private final ServletContext servletContext;
    private final FilterChainProxy springSecurityFilterChain;


    @Override
    public void run(ApplicationArguments args) {
        printServletFilters();
        printSpringSecurityFilters();
    }

    private void printServletFilters() {
        System.out.println("=== [ServletContext Filters] ===");
        Map<String, ? extends FilterRegistration> filters = servletContext.getFilterRegistrations();
        filters.forEach((name, registration) -> {
            System.out.printf("Filter Name: %s%n", name);
            System.out.printf("Class Name: %s%n", registration.getClassName());
            System.out.println("---------------------------------");
        });
    }

    private void printSpringSecurityFilters() {
        System.out.println("=== [Spring Security FilterChainProxy Filters] ===");
        for (SecurityFilterChain chain : springSecurityFilterChain.getFilterChains()) {
            for (Filter filter : chain.getFilters()) {
                System.out.println(" - " + filter.getClass().getName());
            }
            System.out.println("---------------------------------");
        }
    }
}
