package com.happypill.api.config.localeContextResolver;

import com.happypill.api.config.CustomQueryParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HappypillLocaleContextResolver 테스트")
class HappypillLocaleContextResolverTest {

    private final HappypillLocaleContextResolver resolver =
            new HappypillLocaleContextResolver(new CustomLocaleResolver(), new CustomTimeZoneResolver());

    @Test
    @DisplayName("LocaleContext를 정상적으로 생성해야 한다")
    void shouldCreateLocaleContextCorrectly() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "en");
        request.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "America/New_York");

        // when
        LocaleContext context = resolver.resolveLocaleContext(request);

        // then
        assertThat(context.getLocale().getLanguage()).isEqualTo("en");
        assertThat(((TimeZoneAwareLocaleContext) context).getTimeZone().getID()).isEqualTo("America/New_York");
    }
}
