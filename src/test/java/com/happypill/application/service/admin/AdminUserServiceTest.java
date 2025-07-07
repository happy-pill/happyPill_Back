package com.happypill.application.service.admin;

import com.happypill.application.entity.*;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.entity.enums.PaymentMethod;
import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.repository.order.OrderRepository;
import com.happypill.application.repository.orderline.OrderLineRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productinfo.ProductInfoRepository;
import com.happypill.application.repository.subscription.SubscriptionRepository;
import com.happypill.application.service.admin.request.AdminUserUpdateRequest;
import com.happypill.application.service.admin.response.AdminSubscriptionListResponse;
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

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AdminUserServiceTest {

    private static final String UPDATED_NOTIFY_EMAIL = "updatedNotify@gmail.com";
    private static final String UPDATED_NICKNAME = "updatedNick";
    private final Faker faker = new Faker();

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private HappypillUserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

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
        AdminUserDetailResponse response = adminUserService.getUserDetails(savedUser.getId());

        // then
        assertThat(Long.valueOf(response.userId())).isEqualTo(savedUser.getId());
        assertThat(response.provider()).isEqualTo(Provider.KAKAO);
    }

    @Test
    @DisplayName("[회원 정보 수정] 경로 변수의 userId 가 존재하지 않는 회원일 경우 예외를 반환한다.")
    void updateUserProfile_1() {
        //given
        HappypillUser savedUser = generateTestUser();
        userRepository.save(savedUser);

        AdminUserUpdateRequest request = new AdminUserUpdateRequest(UPDATED_NICKNAME, UPDATED_NOTIFY_EMAIL);
        //when //then
        assertThatThrownBy(()->adminUserService.updateUserProfile(1000L,request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[회원 정보 수정] AdminUserUpdateRequest 에서 nickName 값만 있고 notifyEmail 은 null 일 때 nickName 만 수정된다.")
    void updateUserProfile_2() {
        //given
        HappypillUser savedUser = generateTestUser();
        userRepository.save(savedUser);

        AdminUserUpdateRequest request = new AdminUserUpdateRequest(UPDATED_NICKNAME, null);

        //when
        adminUserService.updateUserProfile(savedUser.getId(), request);
        HappypillUser updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();

        // then
        assertThat(updatedUser.getNickName()).isEqualTo(UPDATED_NICKNAME);
    }

    @Test
    @DisplayName("[회원 정보 수정] AdminUserUpdateRequest 에서 nickName, notifyEmail 값 모두 존재할 때 두 필드 값 모두 수정된다.")
    void updateUserProfile_3(){
        //given
        HappypillUser savedUser = generateTestUser();
        userRepository.save(savedUser);

        AdminUserUpdateRequest request = new AdminUserUpdateRequest(UPDATED_NICKNAME, UPDATED_NOTIFY_EMAIL);

        //when
        adminUserService.updateUserProfile(savedUser.getId(), request);
        HappypillUser updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();

        //then
        assertThat(updatedUser.getNickName()).isEqualTo(UPDATED_NICKNAME);
        assertThat(updatedUser.getNotifyEmail()).isEqualTo(UPDATED_NOTIFY_EMAIL);
    }

    @Test
    @DisplayName("[모든 구독 상품 조회] KO 언어로 요청할 경우 상품명이 한글로 출력된다.")
    void getAllSubscriptions_1() {
        //given
        HappypillUser savedUser = generateTestUser();
        userRepository.save(savedUser);

        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx", "https://xxx.com/xxx");
        categoryRepository.save(category);

        List<Product> products = List.of(
                Product.of(SnowflakeUtil.nextId(), 3000, 10, true, "https://xxx.com/xxx", false, category),
                Product.of(SnowflakeUtil.nextId(), 5000, 10, true, "https://xxx.com/xxx", false, category)
        );
        productRepository.saveAll(products);

        List<ProductInfo> productInfos = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명1_KO", "수량 상세1_KO", "경고 메시지1_KO", "사용법1_KO", "https://xxx.com/xxx_KO", "설명1_KO", "회사명1_KO", "간략 설명1_KO", products.get(0)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명1_EN", "수량 상세1_EN", "경고 메시지1_EN", "사용법1_EN", "https://xxx.com/xxx_EN", "설명1_EN", "회사명1_EN", "간략 설명1_EN", products.get(0)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명2_KO", "수량 상세2_KO", "경고 메시지2_KO", "사용법2_KO", "https://xxx.com/xxx_KO", "설명2_KO", "회사명2_KO", "간략 설명2_KO", products.get(1)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명2_EN", "수량 상세2_EN", "경고 메시지2_EN", "사용법2_EN", "https://xxx.com/xxx_EN", "설명2_EN", "회사명2_EN", "간략 설명2_EN", products.get(1))
        );
        productInfoRepository.saveAll(productInfos);

        OrderRecipientInfo orderRecipientInfo = OrderRecipientInfo.create("홍길동", "010-1234-5678", "test@gmail.com");

        List<OrderLine> orderLines = List.of(
                OrderLine.create(1L, 3000, LocalDate.now(), 1, products.get(0)),
                OrderLine.create(2L, 15000, LocalDate.now(), 3, products.get(1))
        );
        Order order = Order.create(SnowflakeUtil.nextId(), "00-00000", PaymentMethod.CARD, orderRecipientInfo, savedUser, orderLines);
        order.complete();
        orderRepository.save(order);
        orderLineRepository.saveAll(orderLines);

        List<Subscription> subscriptions = List.of(
            Subscription.of(SnowflakeUtil.nextId(), ZonedDateTime.now().plusMonths(orderLines.get(0).getMonth()), ZonedDateTime.now().plusMonths(1), false, orderLines.get(0), savedUser),
            Subscription.of(SnowflakeUtil.nextId(), ZonedDateTime.now().plusMonths(orderLines.get(1).getMonth()), ZonedDateTime.now().plusMonths(1), false, orderLines.get(1), savedUser)
        );
        subscriptionRepository.saveAll(subscriptions);

        Pageable pageable = PageRequest.of(0,5);
        Locale locale = Locale.KOREA;

        //when
        CustomPage<AdminSubscriptionListResponse> result = adminUserService.getAllSubscriptions(pageable, locale);

        //then
        assertThat(result.contents())
                .hasSize(2);
        assertThat(result.contents())
                .extracting(AdminSubscriptionListResponse::getSubscriptionId)
                .contains(String.valueOf(subscriptions.get(0).getId()));
    }

    @Test
    @DisplayName("[회원 계정 비활성화/복구] 경로 변수의 userId 가 존재하지 않는 회원일 경우 예외를 반환한다.")
    void updateUserStatus_1() {
        //given
        HappypillUser user = generateTestUser();
        userRepository.save(user);

        //when //then
        assertThatThrownBy(() -> adminUserService.updateUserStatus(0L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[회원 계정 비활성화/복구] 회원을 비활성화 시 isDeleted 필드는 true 로 수정된다.")
    void updateUserStatus_2() {
        //given
        HappypillUser user = generateTestUser();
        userRepository.save(user);

        //when
        adminUserService.updateUserStatus(user.getId());

        //then
        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("[회원 계정 비활성화/복구] 회원을 복구 시 isDeleted 필드는 false 로 수정된다.")
    void updateUserStatus_3() {
        //given
        HappypillUser user = generateTestUser();
        user.deactivate();
        userRepository.save(user);

        //when
        adminUserService.updateUserStatus(user.getId());

        //then
        assertThat(user.isDeleted()).isFalse();
    }
}