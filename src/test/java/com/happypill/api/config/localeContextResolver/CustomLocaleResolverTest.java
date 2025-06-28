package com.happypill.api.config.localeContextResolver;

import com.happypill.api.config.CustomHeaders;
import com.happypill.api.config.CustomQueryParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("CustomLocaleResolver 테스트")
class CustomLocaleResolverTest {

    private final CustomLocaleResolver resolver = new CustomLocaleResolver();

    @Test
    @DisplayName("쿼리 파라미터가 최우선으로 적용되어야 한다")
    void queryParameterShouldHaveHighestPriority() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "en");
        request.addHeader(CustomHeaders.LANGUAGE_HEADER, "ko");
        request.addHeader("Accept-Language", "ja-JP");

        // when
        Locale result = resolver.resolveLocale(request);

        // then
        assertThat(result.getLanguage()).isEqualTo("en");
    }

    @Test
    @DisplayName("헤더가 두 번째 우선순위를 가져야 한다")
    void headerShouldHaveSecondPriority() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CustomHeaders.LANGUAGE_HEADER, "en");
        request.addHeader("Accept-Language", "ko-KR");

        // when
        Locale result = resolver.resolveLocale(request);

        // then
        assertThat(result.getLanguage()).isEqualTo("en");
    }

    @Test
    @DisplayName("Accept-Language 헤더가 마지막 우선순위를 가져야 한다")
    void acceptLanguageHeaderShouldHaveLowestPriority() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "en-US,en;q=0.9");

        // when
        Locale result = resolver.resolveLocale(request);

        // then
        assertThat(result.getLanguage()).isEqualTo("en");
    }

    @Test
    @DisplayName("지원하는 언어(KO, EN)는 정상 처리되어야 한다")
    void supportedLanguagesShouldBeProcessedCorrectly() {
        // given
        MockHttpServletRequest koRequest = new MockHttpServletRequest();
        koRequest.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "ko");

        MockHttpServletRequest enRequest = new MockHttpServletRequest();
        enRequest.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "en");

        // when
        Locale koResult = resolver.resolveLocale(koRequest);
        Locale enResult = resolver.resolveLocale(enRequest);

        // then
        assertAll(
                () -> assertThat(koResult.getLanguage()).isEqualTo("ko"),
                () -> assertThat(enResult.getLanguage()).isEqualTo("en")
        );
    }

    @Test
    @DisplayName("대소문자 무관하게 처리되어야 한다")
    void shouldBeCaseInsensitive() {
        // given
        MockHttpServletRequest upperRequest = new MockHttpServletRequest();
        upperRequest.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "EN");

        MockHttpServletRequest mixedRequest = new MockHttpServletRequest();
        mixedRequest.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "Ko");

        // when
        Locale upperResult = resolver.resolveLocale(upperRequest);
        Locale mixedResult = resolver.resolveLocale(mixedRequest);

        // then
        assertAll(
                () -> assertThat(upperResult.getLanguage()).isEqualTo("en"),
                () -> assertThat(mixedResult.getLanguage()).isEqualTo("ko")
        );
    }

    @Test
    @DisplayName("지원하지 않는 언어는 기본값(ko)으로 처리되고 지역은 유지되어야 한다")
    void unsupportedLanguageShouldFallbackToDefaultWithRegionPreserved() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "fr-FR");

        // when
        Locale result = resolver.resolveLocale(request);

        // then
        assertAll(
                () -> assertThat(result.getLanguage()).isEqualTo("ko"),
                () -> assertThat(result.getCountry()).isEqualTo("FR")
        );
    }

    @Test
    @DisplayName("지역 코드가 없는 지원하지 않는 언어는 기본값(ko)으로 처리되어야 한다")
    void unsupportedLanguageWithoutRegionShouldFallbackToDefault() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "fr");

        // when
        Locale result = resolver.resolveLocale(request);

        // then
        assertAll(
                () -> assertThat(result.getLanguage()).isEqualTo("ko"),
                () -> assertThat(result.getCountry()).isEmpty()
        );
    }

    @Test
    @DisplayName("아무 파라미터도 없으면 기본값(ko)이 적용되어야 한다")
    void shouldUseDefaultWhenNoParameters() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        Locale result = resolver.resolveLocale(request);

        // then
        assertThat(result.getLanguage()).isEqualTo("ko");
    }

    @Test
    @DisplayName("빈 값이나 null 값은 무시되고 다음 우선순위가 적용되어야 한다")
    void emptyOrNullValuesShouldBeIgnored() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "  ");
        request.addHeader(CustomHeaders.LANGUAGE_HEADER, "en");

        // when
        Locale result = resolver.resolveLocale(request);

        // then
        assertThat(result.getLanguage()).isEqualTo("en");
    }

    @Test
    @DisplayName("복합 언어-지역 태그가 정상 처리되어야 한다")
    void complexLanguageTagsShouldBeProcessedCorrectly() {
        // given
        MockHttpServletRequest enUsRequest = new MockHttpServletRequest();
        enUsRequest.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "en-US");

        MockHttpServletRequest koKrRequest = new MockHttpServletRequest();
        koKrRequest.setParameter(CustomQueryParameters.LANGUAGE_PARAMETER, "ko-KR");

        // when
        Locale enUsResult = resolver.resolveLocale(enUsRequest);
        Locale koKrResult = resolver.resolveLocale(koKrRequest);

        // then
        assertAll(
                () -> assertThat(enUsResult.getLanguage()).isEqualTo("en"),
                () -> assertThat(enUsResult.getCountry()).isEqualTo("US"),
                () -> assertThat(koKrResult.getLanguage()).isEqualTo("ko"),
                () -> assertThat(koKrResult.getCountry()).isEqualTo("KR")
        );
    }

    @Test
    @DisplayName("Accept-Language 헤더의 복잡한 형태도 정상 처리되어야 한다")
    void complexAcceptLanguageHeaderShouldBeProcessedCorrectly() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");

        // when
        Locale result = resolver.resolveLocale(request);

        // then
        assertThat(result.getLanguage()).isEqualTo("ko");
    }

}
