package com.happypill.application.entity.enums;

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
            // To do : KO를 business Exception 으로 변경할 것
            return Language.KO;
        }
    }
}
