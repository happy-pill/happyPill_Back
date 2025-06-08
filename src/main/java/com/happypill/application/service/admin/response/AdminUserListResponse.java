package com.happypill.application.service.admin.response;

import com.happypill.application.entity.HappypillUser;

import java.time.format.DateTimeFormatter;

public record AdminUserListResponse(
        Long userId,
        String nickname,
        String loginEmail,
        String provider,
        String createdAt,
        String deletedAt,
        boolean isDeleted
) {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    public static AdminUserListResponse from(HappypillUser user) {
        String createdAt = user.getCreatedAt().format(formatter);
        String deletedAt = user.getUpdatedAt().format(formatter);
        return new AdminUserListResponse(
                user.getUserId(),
                user.getNickName(),
                user.getLoginEmail(),
                user.getProvider().name(),
                createdAt,
                deletedAt,
                user.isDeleted()
        );
    }
}