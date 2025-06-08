package com.happypill.application.entity.enums;

import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;

public enum Language {
    KO("Korean"),
    EN("English");

    private final String description;

    Language(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Language parseLanguage(String language) {
        try {
            return Language.valueOf(language.toUpperCase());
        } catch (Exception e) {
            throw new BusinessException(ExceptionCode.LANGUAGE_NOT_FOUND);
        }
    }
}
