package com.happypill.application.entity;

import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "happypill_users")
public class HappypillUser extends BaseEntity{

    @Id
    private Long userId;

    private String nickName;

    @Enumerated(STRING)
    private Provider provider;

    private String loginEmail;

    private boolean isDeleted;

    @Enumerated(STRING)
    private Role role;
}