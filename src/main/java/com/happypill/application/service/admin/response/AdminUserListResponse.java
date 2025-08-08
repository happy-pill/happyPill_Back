package com.happypill.application.service.admin.response;

import com.happypill.application.entity.HappypillUser;
import com.happypill.application.entity.enums.Provider;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class AdminUserListResponse {
    private String userId;
    private String nickname;
    private String loginEmail;
    private Provider provider;
    private ZonedDateTime createdAt;
    private ZonedDateTime deletedAt;
    private boolean isDeleted;

    @QueryProjection
    public AdminUserListResponse(
            String userId,
            String nickname,
            String loginEmail,
            Provider provider,
            ZonedDateTime createdAt,
            ZonedDateTime deletedAt,
            boolean isDeleted
    ) {
        this.userId = userId;
        this.nickname = nickname;
        this.loginEmail = loginEmail;
        this.provider = provider;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.isDeleted = isDeleted;
    }

    public static AdminUserListResponse from(HappypillUser user) {
        return new AdminUserListResponse(
                user.getId().toString(),
                user.getNickname(),
                user.getLoginEmail(),
                user.getProvider(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isDeleted()
        );
    }
}