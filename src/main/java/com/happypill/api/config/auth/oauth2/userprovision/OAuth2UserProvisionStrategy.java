package com.happypill.api.config.auth.oauth2.userprovision;

import com.happypill.application.entity.HappypillUser;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public abstract class OAuth2UserProvisionStrategy {

    private final HappypillUserRepository repository;

    public HappypillUser provision(OAuth2User oAuth2User) {
        String sub = extractSub(oAuth2User);
        if (sub == null) {
            throw new RuntimeException("sub cannot be null");
        }
        return repository.findBySocialSub(sub)
                .orElseGet(() -> repository.save(createUser(oAuth2User)));
    }

    protected abstract String extractSub(OAuth2User oAuth2User);


    public abstract boolean supports(String provider);

    protected abstract HappypillUser createUser(OAuth2User oAuth2User);
}
