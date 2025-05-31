package com.happypill.api.config.auth.oauth2.userprovision;

import com.happypill.application.entity.HappypillUser;
import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.util.SnowflakeUtil;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class KakaoProvisionStrategy extends OAuth2UserProvisionStrategy {

    public KakaoProvisionStrategy(HappypillUserRepository repository) {
        super(repository);
    }

    @Override
    protected String extractSub(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("sub");
    }

    @Override
    public boolean supports(String provider) {
        return provider.equalsIgnoreCase(Provider.KAKAO.name());
    }

    @Override
    protected HappypillUser createUser(OAuth2User oAuth2User) {
        String sub = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        return HappypillUser.ofSocial(
                SnowflakeUtil.nextId(),
                null,
                Provider.KAKAO,
                sub,
                email,
                email,
                Role.USER
        );
    }
}
