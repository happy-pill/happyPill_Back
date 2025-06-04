package com.happypill.application.pagination;

import org.springframework.data.domain.Page;

import java.util.List;

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
}
