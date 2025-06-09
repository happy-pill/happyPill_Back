package com.happypill.application.service.admin.response;

import com.happypill.application.entity.HappypillUser;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public record AdminUserListResponse(
        String userId,
        String nickname,
        String loginEmail,
        String provider,
        ZonedDateTime createdAt,
        ZonedDateTime deletedAt,
        boolean isDeleted
) {

    public static AdminUserListResponse from(HappypillUser user) {
        return new AdminUserListResponse(
                user.getUserId().toString(),
                user.getNickName(),
                user.getLoginEmail(),
                user.getProvider().name(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isDeleted()
        );
    }
}