package com.happypill.api.config.localeContextResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleContextResolver;

import java.util.Locale;
import java.util.TimeZone;

@Component("localeResolver")
@RequiredArgsConstructor
public class HappypillLocaleContextResolver implements LocaleContextResolver {

    private final CustomLocaleResolver localeResolver;
    private final CustomTimeZoneResolver timeZoneResolver;

    @Override
    public LocaleContext resolveLocaleContext(HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        TimeZone timeZone = timeZoneResolver.resolveTimeZone(request);
        return new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
        throw new AssertionError("unreachable");
    }
}
