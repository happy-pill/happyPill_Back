package com.happypill.application.service.user.response;

import com.happypill.application.entity.HappypillUser;
import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;

import java.time.ZonedDateTime;

public record UserInfoResponse(
        String userId,
        Provider provider,
        String nickname,
        String loginEmail,
        String notifyEmail,
        Role role,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
    public static UserInfoResponse from(HappypillUser happypillUser) {
        return new UserInfoResponse(
                String.valueOf(happypillUser.getUserId()),
                happypillUser.getProvider(),
                happypillUser.getNickName(),
                happypillUser.getLoginEmail(),
                happypillUser.getNotifyEmail(),
                happypillUser.getRole(),
                happypillUser.getCreatedAt(),
                happypillUser.getUpdatedAt()
        );
    }
}
