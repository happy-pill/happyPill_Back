package com.happypill.api.config.localeContextResolver;

import com.happypill.api.config.CustomHeaders;
import com.happypill.api.config.CustomQueryParameters;
import com.happypill.application.entity.enums.Language;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

import static org.springframework.util.StringUtils.hasText;

/**
 * HappyPill 애플리케이션용 커스텀 LocaleResolver
 * <p>
 * 우선순위:
 * 1. language 쿼리 파라미터
 * 2. Language 커스텀 헤더
 * 3. Accept-Language 헤더 (default 국어)
 * 만약 지원하지 않는 언어일경우, 지역은 그대로 놔두고, 국가는 통일
 */
@Component
public class CustomLocaleResolver implements LocaleResolver {

    private static final Locale DEFAULT_LOCALE = Locale.KOREA;
    private final AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();

    public CustomLocaleResolver() {
        acceptHeaderLocaleResolver.setDefaultLocale(DEFAULT_LOCALE);
    }

    @NotNull
    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String langParam = request.getParameter(CustomQueryParameters.LANGUAGE_PARAMETER);
        if (hasText(langParam)) {
            return validateLanguageOrDefault(Locale.forLanguageTag(langParam));
        }
        String langHeader = request.getHeader(CustomHeaders.LANGUAGE_HEADER);
        if (hasText(langHeader)) {
            return validateLanguageOrDefault(Locale.forLanguageTag(langHeader));
        }

        return acceptHeaderLocaleResolver.resolveLocale(request);
    }

    private Locale validateLanguageOrDefault(Locale locale) {
        for (Language language : Language.values()) {
            if (language.name().equalsIgnoreCase(locale.getLanguage())) {
                return locale;
            }
        }
        return new Locale.Builder()
                .setLanguage(DEFAULT_LOCALE.getLanguage())
                .setRegion(locale.getCountry())
                .build();
    }


    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        // 저장안할거
    }

}
