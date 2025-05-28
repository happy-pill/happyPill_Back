package com.happypill.api.config.auth.usercontext;

import com.happypill.application.auth.UserContext;
import lombok.AllArgsConstructor;

import java.security.Principal;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class SecurityUserContext implements UserContext, Principal {

    private final Long userId;

    public static SecurityUserContext from(Long id) {
        return new SecurityUserContext(id);
    }

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}
