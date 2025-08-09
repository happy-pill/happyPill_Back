package com.happypill.application.service.admin.response;

import com.happypill.application.entity.HappypillUser;
import com.happypill.application.entity.enums.Provider;

import java.time.ZonedDateTime;

public record AdminUserDetailResponse(
        String userId,
        String loginEmail,
        String nickname,
        String notifyEmail,
        Provider provider,
        ZonedDateTime createdAt,
        ZonedDateTime deletedAt,
        boolean isDeleted
) {

    public static AdminUserDetailResponse from(HappypillUser user) {
        return new AdminUserDetailResponse(
                user.getId().toString(),
                user.getLoginEmail(),
                user.getNickname(),
                user.getNotifyEmail(),
                user.getProvider(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isDeleted()
        );
    }
}