package com.happypill.application.service.admin;

import com.happypill.application.entity.HappypillUser;
import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.service.admin.request.AdminUserUpdateRequest;
import com.happypill.application.service.admin.response.AdminUserDetailResponse;
import com.happypill.application.service.admin.response.AdminUserListResponse;
import com.happypill.application.util.SnowflakeUtil;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AdminUserServiceTest {

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

    @Test
    @DisplayName("[모든 회원 조회] 모든 회원 목록을 페이지네이션하여 반환한다.")
    void getAllUsers_1() {
        //given
        List<HappypillUser> happypillUserList = List.of(generateTestUser(), generateTestUser());
        userRepository.saveAll(happypillUserList);
        Pageable pageable = PageRequest.of(0, 5);

        //when
        CustomPage<AdminUserListResponse> response = adminUserService.getAllUsers(pageable);

        //then
        assertThat(response.contents()).hasSize(happypillUserList.size());
    }

    @Test
    @DisplayName("[모든 회원 조회] 회원이 0명인 경우 에러는 발생하지 않으며 CustomPage 의 contents 사이즈는 0으로 반환한다.")
    void getAllUsers_2() {
        //given
        Pageable pageable = PageRequest.of(0, 5);

        //when
        CustomPage<AdminUserListResponse> response = adminUserService.getAllUsers(pageable);

        //then
        assertThat(response.contents()).hasSize(0);
    }

    @Test
    @DisplayName("[특정 회원 조회] 경로변수의 userId 가 존재하지 않는 회원일 경우 에러를 반환한다.")
    void getUserDetails_1() {
        //given
        HappypillUser user = generateTestUser();
        userRepository.save(user);

        //when //then
        assertThatThrownBy(() -> adminUserService.getUserDetails(1000L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[특정 회원 조회] 경로변수의 userId 가 존재하는 회원일 경우 AdminUserDetailResponse 를 반환한다.")
    void getUserDetails_2() {
        //given
        HappypillUser savedUser = generateTestUser();
        userRepository.save(savedUser);

        //when
        AdminUserDetailResponse response = adminUserService.getUserDetails(savedUser.getUserId());

        // then
        assertThat(Long.valueOf(response.userId())).isEqualTo(savedUser.getUserId());
        assertThat(response.provider()).isEqualTo(Provider.KAKAO);
    }

    @Test
    @DisplayName("[회원 정보 수정] 특정 회원의 notifyEmail 를 수정할 때 다른 회원들의 loginEmail, notifyEmail 과 중복될 경우 에러를 반환한다.")
    void updateUserProfile_1() {
        //given
        HappypillUser savedUser = HappypillUser.ofSocial(SnowflakeUtil.nextId(), "nick_xxx", Provider.KAKAO, "sub_xxxxx", "login@gmail.com", "notify@gmail.com", Role.USER);
        List<HappypillUser> userList = List.of(
                HappypillUser.ofSocial(SnowflakeUtil.nextId(), "nick_1", Provider.KAKAO, "sub_1", "login1@gmail.com", "notify1@gmail.com", Role.USER),
                HappypillUser.ofSocial(SnowflakeUtil.nextId(), "nick_2", Provider.KAKAO, "sub_2", "login2@gmail.com", "notify2@gmail.com", Role.USER)
        );
        userRepository.save(savedUser);
        userRepository.saveAll(userList);

        AdminUserUpdateRequest request = new AdminUserUpdateRequest("testNick", "notify2@gmail.com");

        //when //then
        assertThatThrownBy(() -> adminUserService.updateUserProfile(savedUser.getUserId(), request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.EMAIL_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("[회원 정보 수정] 특정 회원의 notifyEmail 를 수정할 때 자신의 loginEmail 과 같은 경우 정상적으로 수정된다.")
    void updateUserProfile_2() {
        //given
        HappypillUser savedUser = HappypillUser.ofSocial(SnowflakeUtil.nextId(), "nick_xxx", Provider.KAKAO, "sub_xxxxx", "login@gmail.com", "notify@gmail.com", Role.USER);
        userRepository.save(savedUser);

        AdminUserUpdateRequest request = new AdminUserUpdateRequest(null, "login@gmail.com");

        //when
        adminUserService.updateUserProfile(savedUser.getUserId(), request);
        HappypillUser updatedUser = userRepository.findById(savedUser.getUserId()).orElseThrow();

        // then
        assertThat(updatedUser.getNotifyEmail()).isEqualTo("login@gmail.com");
    }

    @Test
    @DisplayName("[회원 정보 수정] AdminUserUpdateRequest 에서 nickName 값만 있고 notifyEmail 은 null 일 때 nickName 만 수정된다.")
    void updateUserProfile_3() {
        //given
        HappypillUser savedUser = HappypillUser.ofSocial(SnowflakeUtil.nextId(), "nick_xxx", Provider.KAKAO, "sub_xxxxx", "login@gmail.com", "notify@gmail.com", Role.USER);
        userRepository.save(savedUser);

        AdminUserUpdateRequest request = new AdminUserUpdateRequest("testNick", null);

        //when
        adminUserService.updateUserProfile(savedUser.getUserId(), request);
        HappypillUser updatedUser = userRepository.findById(savedUser.getUserId()).orElseThrow();

        // then
        assertThat(updatedUser.getNickName()).isEqualTo("testNick");
        assertThat(updatedUser.getLoginEmail()).isEqualTo("login@gmail.com");
        assertThat(updatedUser.getNotifyEmail()).isEqualTo("notify@gmail.com");
        assertThat(updatedUser.getProvider()).isEqualTo(Provider.KAKAO);
        assertThat(updatedUser.getSocialSub()).isEqualTo("sub_xxxxx");
    }
}