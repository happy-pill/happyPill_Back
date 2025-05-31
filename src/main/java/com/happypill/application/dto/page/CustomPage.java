package com.happypill.application.dto.page;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record CustomPage<T>(
        List<T> contents,
        int page,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {

    public CustomPage(Page<T> page) {
        this(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // Page<entity> → Page<dto> → Custom<dto> 로 변환하는 메서드
    public static <T, R> CustomPage<R> mapFrom(Page<T> page, Function<T, R> mapper) {
        Page<R> mappedPage = page.map(mapper);
        return new CustomPage<>(mappedPage);
    }
}