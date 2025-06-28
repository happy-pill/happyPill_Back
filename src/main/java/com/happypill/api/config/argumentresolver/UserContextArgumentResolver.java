package com.happypill.api.config.argumentresolver;

import com.happypill.api.config.auth.usercontext.HappypillUser;
import com.happypill.application.auth.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class UserContextArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(HappypillUser.class) &&
                UserContext.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserContext userContext) {
            return userContext;
        }
        log.warn("이 메시지가 표시된다는 것은, 인증되지 않은 사용자가 인증이 필요한 경로에 접근했다는 뜻입니다\n 백엔드에선 401을 반환하지만, 프론트에게 알려주세요.");
        return null;
    }

}
