package com.happypill.api.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomQueryParameters {
    public static final String LANGUAGE_PARAMETER = "language";

    public static final String TIMEZONE_PARAMETER = "timeZone";
}
