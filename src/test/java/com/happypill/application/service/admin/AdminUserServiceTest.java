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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

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
    @DisplayName("[모든 구독 상품 조회] 모든 구독 상품 목록 포함한 dto 를 반환한다.")
    void getAllSubscriptions_1(){
        //given
        HappypillUser savedUser = generateTestUser();
        userRepository.save(savedUser);

        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);


        Product p1 = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        Product p2 = Product.of(SnowflakeUtil.nextId(), 3000, 3, true, " https://xxx.com/xxx", false, category);
        List<Product> productList = List.of(p1, p2);
        productRepository.saveAll(productList);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명1_KO", "수량 상세1_KO", "경고 메시지1_KO", "사용법1_KO", "https://xxx.com/xxx_KO", "설명1_KO", "회사명1_KO", "간략 설명1_KO", p1),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명1_EN", "수량 상세1_EN", "경고 메시지1_EN", "사용법1_EN", "https://xxx.com/xxx_EN", "설명1_EN", "회사명1_EN", "간략 설명1_EN", p1),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명2_KO", "수량 상세2_KO", "경고 메시지2_KO", "사용법2_KO", "https://xxx.com/xxx_KO", "설명2_KO", "회사명2_KO", "간략 설명2_KO", p2),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명2_EN", "수량 상세2_EN", "경고 메시지2_EN", "사용법2_EN", "https://xxx.com/xxx_EN", "설명2_EN", "회사명2_EN", "간략 설명2_EN", p2)
        );
        productInfoRepository.saveAll(productInfo);

        OrderRecipientInfo orderRecipientInfo = OrderRecipientInfo.create("홍길동", "010-1234-5678", "test@gmail.com");

        LocalDate startDate = LocalDate.now();

        int month = 3;

        LocalDate plusDate = startDate.plusMonths(month);

        ZoneId zone = ZoneId.of("Asia/Seoul");
        ZonedDateTime expiredDate = plusDate.atStartOfDay(zone);
        ZonedDateTime nextDeliveryDate = startDate.atStartOfDay(zone).plusMonths(1);

        OrderLine o1 = OrderLine.create(SnowflakeUtil.nextId(), 1000, startDate, month, p1);
        OrderLine o2 = OrderLine.create(SnowflakeUtil.nextId(), 3000, startDate, month, p2);
        List<OrderLine> orderLineList = List.of(o1, o2);

        Order order = Order.create(SnowflakeUtil.nextId(), "xx-xxxxx", PaymentMethod.CARD, orderRecipientInfo, savedUser, orderLineList);
        order.complete();
        orderRepository.save(order);

        List<Subscription> subscriptionList = new ArrayList<>();
        Subscription s1 = Subscription.of(SnowflakeUtil.nextId(), expiredDate, nextDeliveryDate, false, o1, savedUser);
        Subscription s2 = Subscription.of(SnowflakeUtil.nextId(), expiredDate, nextDeliveryDate, false, o2, savedUser);
        subscriptionList.add(s1);
        subscriptionList.add(s2);
        subscriptionRepository.saveAll(subscriptionList);

        Locale locale = Locale.forLanguageTag("ko");
        Pageable pageable = PageRequest.of(0, 8);

        //when
        CustomPage<AdminSubscriptionListResponse> response = adminUserService.getAllSubscriptions(pageable, locale);

        //then
        assertThat(response.contents())
                .extracting(AdminSubscriptionListResponse::subscriptionId)
                .contains(String.valueOf(s1.getId()));

    }
}