package com.happypill.application.service.admin;

import com.happypill.application.entity.HappypillUser;
import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.service.admin.response.AdminUserListResponse;
import com.happypill.application.util.SnowflakeUtil;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AdminUserServiceTest {

    private static final int size = 10;

    private final Faker faker = new Faker();

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private HappypillUserRepository userRepository;

    private HappypillUser generateTestUser() {
        String loginEmail = faker.internet().emailAddress();
        String notifyEmail = faker.internet().emailAddress();
        String nickName = faker.name().name();
        String sub = faker.idNumber().valid();
        return HappypillUser.ofSocial(
                SnowflakeUtil.nextId(),
                nickName,
                Provider.KAKAO,
                sub,
                loginEmail,
                notifyEmail,
                Role.USER
        );
    }

    @BeforeEach
    void setUp() {
        List<HappypillUser> userList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            userList.add(generateTestUser());
        }
        userRepository.saveAll(userList);
    }

    @Test
    @DisplayName("[모든 회원 조회] 모든 회원 목록을 페이지네이션하여 반환한다.")
    void getAllUsers_1() {
        //given
        Pageable pageable = PageRequest.of(0, size);

        //when
        CustomPage<AdminUserListResponse> response = adminUserService.getAllUsers(pageable);

        //then
        assertThat(response.contents()).hasSize(size);
    }

    @Test
    @DisplayName("[모든 회원 조회] 회원이 0명인 경우 CustomPage 의 contents 사이즈는 0으로 반환한다.")
    void getAllUsers_2() {
        //given
        userRepository.deleteAllInBatch();
        Pageable pageable = PageRequest.of(0, size);

        //when
        CustomPage<AdminUserListResponse> response = adminUserService.getAllUsers(pageable);

        //then
        assertThat(response.contents()).hasSize(0);
    }
}