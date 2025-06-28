package com.happypill.api.config.localeContextResolver;

import com.happypill.api.config.CustomHeaders;
import com.happypill.api.config.CustomQueryParameters;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

import static org.springframework.util.StringUtils.hasText;


/**
 * HappyPill 애플리케이션용 커스텀 TimeZoneResolver
 * <p>
 * 우선순위:
 * 1. ?timeZone 쿼리파라미터
 * 2. APP-TIME-ZONE 커스텀 헤더
 * 3. 한국시간
 */
@Component
public class CustomTimeZoneResolver {

    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();

    public TimeZone resolveTimeZone(HttpServletRequest request) {

        String timeZoneId;

        timeZoneId = request.getParameter(CustomQueryParameters.TIMEZONE_PARAMETER);
        if (hasText(timeZoneId)) {
            return validateTimeZoneOrDefault(timeZoneId);
        }

        timeZoneId = request.getHeader(CustomHeaders.TIMEZONE_HEADER);
        if (hasText(timeZoneId)) {
            return validateTimeZoneOrDefault(timeZoneId);
        }
        return DEFAULT_TIMEZONE;
    }

    private TimeZone validateTimeZoneOrDefault(String timeZone) {
        for (var t : TimeZone.getAvailableIDs()) {
            if (t.equalsIgnoreCase(timeZone)) {
                return TimeZone.getTimeZone(t);
            }
        }
        return DEFAULT_TIMEZONE;
    }
}
