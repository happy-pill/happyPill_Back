package com.happypill.application.service.category.dto.response;

import com.happypill.application.entity.enums.Language;

import java.util.Map;

public record CategoryNamesResponse(
        String categoryId,
        Map<Language, String> names
) {
}