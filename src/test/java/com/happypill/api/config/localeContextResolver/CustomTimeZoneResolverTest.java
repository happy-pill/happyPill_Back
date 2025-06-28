package com.happypill.api.config.localeContextResolver;

import com.happypill.api.config.CustomHeaders;
import com.happypill.api.config.CustomQueryParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("CustomTimeZoneResolver 테스트")
class CustomTimeZoneResolverTest {

    private final CustomTimeZoneResolver resolver = new CustomTimeZoneResolver();

    @Test
    @DisplayName("쿼리 파라미터가 최우선으로 적용되어야 한다")
    void queryParameterShouldHaveHighestPriority() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "America/New_York");
        request.addHeader(CustomHeaders.TIMEZONE_HEADER, "Asia/Tokyo");

        // when
        TimeZone result = resolver.resolveTimeZone(request);

        // then
        assertThat(result.getID()).isEqualTo("America/New_York");
    }

    @Test
    @DisplayName("헤더가 두 번째 우선순위를 가져야 한다")
    void headerShouldHaveSecondPriority() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CustomHeaders.TIMEZONE_HEADER, "Europe/London");

        // when
        TimeZone result = resolver.resolveTimeZone(request);

        // then
        assertThat(result.getID()).isEqualTo("Europe/London");
    }

    @Test
    @DisplayName("유효한 타임존들이 정상 처리되어야 한다")
    void validTimeZonesShouldBeProcessedCorrectly() {
        // given
        MockHttpServletRequest asiaSeoulRequest = new MockHttpServletRequest();
        asiaSeoulRequest.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "Asia/Seoul");

        MockHttpServletRequest americaNewYorkRequest = new MockHttpServletRequest();
        americaNewYorkRequest.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "America/New_York");

        MockHttpServletRequest europeLondonRequest = new MockHttpServletRequest();
        europeLondonRequest.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "Europe/London");

        MockHttpServletRequest utcRequest = new MockHttpServletRequest();
        utcRequest.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "UTC");

        // when
        TimeZone asiaSeoulResult = resolver.resolveTimeZone(asiaSeoulRequest);
        TimeZone americaNewYorkResult = resolver.resolveTimeZone(americaNewYorkRequest);
        TimeZone europeLondonResult = resolver.resolveTimeZone(europeLondonRequest);
        TimeZone utcResult = resolver.resolveTimeZone(utcRequest);

        // then
        assertAll(
                () -> assertThat(asiaSeoulResult.getID()).isEqualTo("Asia/Seoul"),
                () -> assertThat(americaNewYorkResult.getID()).isEqualTo("America/New_York"),
                () -> assertThat(europeLondonResult.getID()).isEqualTo("Europe/London"),
                () -> assertThat(utcResult.getID()).isEqualTo("UTC")
        );
    }

    @Test
    @DisplayName("대소문자 무관하게 처리되어야 한다")
    void shouldBeCaseInsensitive() {
        // given
        MockHttpServletRequest lowerCaseRequest = new MockHttpServletRequest();
        lowerCaseRequest.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "asia/seoul");

        MockHttpServletRequest upperCaseRequest = new MockHttpServletRequest();
        upperCaseRequest.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "AMERICA/NEW_YORK");

        MockHttpServletRequest mixedCaseRequest = new MockHttpServletRequest();
        mixedCaseRequest.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "Europe/LONDON");

        // when
        TimeZone lowerCaseResult = resolver.resolveTimeZone(lowerCaseRequest);
        TimeZone upperCaseResult = resolver.resolveTimeZone(upperCaseRequest);
        TimeZone mixedCaseResult = resolver.resolveTimeZone(mixedCaseRequest);

        // then
        assertAll(
                () -> assertThat(lowerCaseResult.getID()).isEqualTo("Asia/Seoul"),
                () -> assertThat(upperCaseResult.getID()).isEqualTo("America/New_York"),
                () -> assertThat(mixedCaseResult.getID()).isEqualTo("Europe/London")
        );
    }

    @Test
    @DisplayName("유효하지 않은 타임존은 시스템 기본값으로 처리되어야 한다")
    void invalidTimeZoneShouldFallbackToDefault() {
        // given
        MockHttpServletRequest invalidRequest = new MockHttpServletRequest();
        invalidRequest.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "Invalid/TimeZone");

        MockHttpServletRequest nonExistentRequest = new MockHttpServletRequest();
        nonExistentRequest.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "NotExist/Zone");

        // when
        TimeZone invalidResult = resolver.resolveTimeZone(invalidRequest);
        TimeZone nonExistentResult = resolver.resolveTimeZone(nonExistentRequest);

        // then
        assertAll(
                () -> assertThat(invalidResult).isEqualTo(TimeZone.getDefault()),
                () -> assertThat(nonExistentResult).isEqualTo(TimeZone.getDefault())
        );
    }

    @Test
    @DisplayName("아무 파라미터도 없으면 시스템 기본값이 적용되어야 한다")
    void shouldUseDefaultWhenNoParameters() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        TimeZone result = resolver.resolveTimeZone(request);

        // then
        assertThat(result).isEqualTo(TimeZone.getDefault());
    }

    @Test
    @DisplayName("빈 값이나 null 값은 무시되고 다음 우선순위가 적용되어야 한다")
    void emptyOrNullValuesShouldBeIgnored() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "");
        request.addHeader(CustomHeaders.TIMEZONE_HEADER, "Europe/Paris");

        // when
        TimeZone result = resolver.resolveTimeZone(request);

        // then
        assertThat(result.getID()).isEqualTo("Europe/Paris");
    }

    @Test
    @DisplayName("whitespace만 있는 값은 무시되고 다음 우선순위가 적용되어야 한다")
    void whitespaceOnlyValuesShouldBeIgnored() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "   ");
        request.addHeader(CustomHeaders.TIMEZONE_HEADER, "Asia/Tokyo");

        // when
        TimeZone result = resolver.resolveTimeZone(request);

        // then
        assertThat(result.getID()).isEqualTo("Asia/Tokyo");
    }


    @Test
    @DisplayName("쿼리 파라미터와 헤더에 서로 다른 값이 있을 때 쿼리 파라미터가 우선되어야 한다")
    void queryParameterShouldOverrideHeader() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "America/Los_Angeles");
        request.addHeader(CustomHeaders.TIMEZONE_HEADER, "Asia/Shanghai");

        // when
        TimeZone result = resolver.resolveTimeZone(request);

        // then
        assertThat(result.getID()).isEqualTo("America/Los_Angeles");
    }

    @Test
    @DisplayName("유효하지 않은 쿼리 파라미터가 있을 때 헤더 값이 사용되지 않고 기본값이 적용되어야 한다")
    void invalidQueryParameterShouldNotFallbackToHeader() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "Invalid/Zone");
        request.addHeader(CustomHeaders.TIMEZONE_HEADER, "Asia/Seoul");

        // when
        TimeZone result = resolver.resolveTimeZone(request);

        // then
        assertThat(result).isEqualTo(TimeZone.getDefault());
    }

    @Test
    @DisplayName("StringUtils.isNoneEmpty 메서드가 올바르게 동작하는지 확인")
    void stringUtilsIsNoneEmptyBehavior() {
        // given
        MockHttpServletRequest nullRequest = new MockHttpServletRequest();
        // parameter를 설정하지 않으면 null 반환

        MockHttpServletRequest emptyRequest = new MockHttpServletRequest();
        emptyRequest.setParameter(CustomQueryParameters.TIMEZONE_PARAMETER, "");

        // when
        TimeZone nullResult = resolver.resolveTimeZone(nullRequest);
        TimeZone emptyResult = resolver.resolveTimeZone(emptyRequest);

        // then
        assertAll(
                () -> assertThat(nullResult).isEqualTo(TimeZone.getDefault()),
                () -> assertThat(emptyResult).isEqualTo(TimeZone.getDefault())
        );
    }
}
