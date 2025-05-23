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
}
