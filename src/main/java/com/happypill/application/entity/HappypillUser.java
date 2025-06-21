package com.happypill.application.entity;

import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import jakarta.persistence.*;
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
public class HappypillUser extends BaseEntity<Long> {

    @Id
    private Long id;

    private String nickName;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String socialSub;

    @Column(nullable = false)
    private String loginEmail;

    @Column(nullable = false)
    private String notifyEmail;

    @Column(nullable = false)
    private boolean isDeleted;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Role role;

    public static HappypillUser ofSocial(Long id, String nickName, Provider provider, String socialSub, String loginEmail, String notifyEmail, Role role) {
        return new HappypillUser(id, nickName, provider, socialSub, loginEmail, notifyEmail, false, role);
    }

    public void changeUser(String nickName) {
        this.nickName = nickName;
    }

    public void changeNotifyEmail(String notifyEmail) {
        this.notifyEmail = notifyEmail;
    }
}