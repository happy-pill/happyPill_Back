package com.happypill.application.entity;

import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Table(name = "happypill_users")
public class HappypillUser extends BaseEntity {

    @Id
    private Long userId;

    private String nickName;

    @Enumerated(STRING)
    private Provider provider;

    private String socialSub;

    private String loginEmail;

    private String notifyEmail;

    private boolean isDeleted;

    @Enumerated(STRING)
    private Role role;

    public static HappypillUser ofSocial(@Nullable Long id, String nickName, Provider provider, String socialSub, String loginEmail, Role role) {
        return new HappypillUser(id, nickName, provider, socialSub, loginEmail, loginEmail,false, role);
    }
}