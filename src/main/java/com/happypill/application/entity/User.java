package com.happypill.application.entity;

import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
public class User extends BaseEntity{

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nick_name")
    private String nickName;

    private Provider provider;

    @Column(name = "login_email")
    private String loginEmail;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany
    private List<Order> orders;

    @OneToMany
    private List<Subscription> subscriptions;
}