package com.happypill.api.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomHeaders {

    public static final String LANGUAGE_HEADER = "APP-LANGUAGE";

    public static final String TIMEZONE_HEADER = "APP-TIME-ZONE";

}
