package com.happypill.application.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Language {
    KO("Korean"),
    EN("English");

    private final String description;

    public static Language parseLanguage(String language) {
        for (var l : Language.values()) {
            if (l.name().equalsIgnoreCase(language)) {
                return l;
            }
        }
        return KO;
    }
}
