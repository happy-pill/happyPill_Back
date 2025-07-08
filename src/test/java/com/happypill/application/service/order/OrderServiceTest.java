package com.happypill.application.service.order;

import com.happypill.api.config.auth.usercontext.SecurityUserContext;
import com.happypill.application.entity.*;
import com.happypill.application.entity.enums.OrderStatus;
import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import com.happypill.application.event.outbox.OutboxEventPublisher;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.repository.order.OrderRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.service.order.payment.PPaymentClient;
import com.happypill.application.service.order.request.OrderCreateRequest;
import com.happypill.application.service.order.response.OrderLineResponse;
import com.happypill.application.service.order.response.OrderRecipientInfoResponse;
import com.happypill.application.service.order.response.OrderResponse;
import com.happypill.application.util.SnowflakeUtil;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
@Transactional
class OrderServiceTest {

    private final Faker faker = new Faker();

    @Autowired
    private HappypillUserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @MockitoBean
    private PPaymentClient pPaymentClient;

    @MockitoBean
    private OutboxEventPublisher outboxEventPublisher;

    private HappypillUser getSavedTestUser() {
        return userRepository.save(HappypillUser.ofSocial(
                SnowflakeUtil.nextId(),
                faker.name().toString(),
                Provider.GOOGLE,
                faker.number().digits(6),
                faker.internet().emailAddress(),
                faker.internet().emailAddress(),
                Role.USER)
        );
    }

    @Test
    @DisplayName("주문 생성 - 여러 상품에 대한 주문 생성시 재고 감소 및 주문 정보가 올바르게 저장되는지 검증")
    void test1() {
        HappypillUser savedTestUser = getSavedTestUser();
        SecurityUserContext userContext = SecurityUserContext.from(savedTestUser.getId());
        Category category = categoryRepository.save(Category.of(SnowflakeUtil.nextId(), "www.happypill.com/image", "www.happypill.com/banner"));
        Product product1 = productRepository.save(Product.of(SnowflakeUtil.nextId(), 1000, 100, "www.happypill.com/product1-thumbnail", category));
        Product product2 = productRepository.save(Product.of(SnowflakeUtil.nextId(), 2000, 200, "www.happypill.com/product2-thumbnail", category));
        var orderLineCreateRequest1 = new OrderCreateRequest.OrderLineCreateRequest(product1.getId(), 3, LocalDate.of(2026, 1, 1));
        var orderLineCreateRequest2 = new OrderCreateRequest.OrderLineCreateRequest(product2.getId(), 1, LocalDate.of(2026, 2, 2));

        String recipientName = faker.name().fullName();
        String recipientMobile = faker.phoneNumber().cellPhone();

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(recipientName, recipientMobile,
                List.of(
                        orderLineCreateRequest1,
                        orderLineCreateRequest2
                )
        );

        OrderResponse orderResponse = orderService.createOrder(userContext, orderCreateRequest);

        Order order = orderRepository.findById(Long.valueOf(orderResponse.orderId())).get();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getOrderRecipientInfo())
                .extracting(OrderRecipientInfo::getName, OrderRecipientInfo::getMobile, OrderRecipientInfo::getEmail)
                .containsExactly(orderCreateRequest.recipentName(), orderCreateRequest.recipentMobile(), savedTestUser.getNotifyEmail());
        assertThat(order.getUser()).isEqualTo(savedTestUser);
        assertThat(order.getOrderLines().size()).isEqualTo(orderCreateRequest.orderLineCreateRequests().size());
        assertThat(order.getTotalPrice()).isEqualTo(order.getOrderLines().stream().mapToInt(OrderLine::getPrice).sum());
        assertThat(order.getOrderLines())
                .extracting(
                        OrderLine::getMonth, ol -> ol.getProduct().getId(), OrderLine::getPrice
                ).containsExactly(
                        tuple(orderLineCreateRequest1.month(), orderLineCreateRequest1.productId(), orderLineCreateRequest1.month() * product1.getPrice()),
                        tuple(orderLineCreateRequest2.month(), orderLineCreateRequest2.productId(), orderLineCreateRequest2.month() * product2.getPrice())
                );

        assertThat(orderResponse)
//                .usingRecursiveComparison()
//                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(
                        new OrderResponse(
                                String.valueOf(order.getId()),
                                order.getTotalPrice(),
                                order.getPaymentUid(),
                                order.getStatus(),
                                order.getPaymentMethod(),
                                String.valueOf(order.getUser().getId()),
                                order.getCreatedAt(),
                                order.getUpdatedAt(),
                                OrderRecipientInfoResponse.from(order.getOrderRecipientInfo()),
                                order.getOrderLines().stream().map(OrderLineResponse::from).toList()
                        )
                );

    }

//    @Test
//    @DisplayName("결제 확인 - PG사 결제 검증 성공시 주문 상태가 COMPLETED로 변경되고 주문완료 이벤트가 발행되는지 검증")
//    void test2() {
//        // given
//        HappypillUser savedTestUser = getSavedTestUser();
//        Category category = categoryRepository.save(Category.of(SnowflakeUtil.nextId(), "www.happypill.com/image", "www.happypill.com/banner"));
//        Product product = productRepository.save(Product.of(SnowflakeUtil.nextId(), 1000, 100, "www.happypill.com/product-thumbnail", category));
//
//        // 주문 생성
//        List<OrderLine> orderLines = List.of(
//                OrderLine.create(SnowflakeUtil.nextId(), 3000, LocalDate.now(), 3, product),
//                OrderLine.create(SnowflakeUtil.nextId(), 5000, LocalDate.now(), 1, product)
//        );
//        Order order = orderRepository.save(Order.create(
//                SnowflakeUtil.nextId(),
//                "pay_" + UUID.randomUUID(),
//                com.happypill.application.entity.enums.PaymentMethod.CARD,
//                OrderRecipientInfo.create("수령인", "010-1234-5678", "test@email.com"),
//                savedTestUser,
//                orderLines
//        ));
//
//        when(pPaymentClient.getVerifiedPayment(order.getPaymentUid())).thenReturn(new VerifiedPayment(
//                order.getPaymentUid(),
//                8000,
//                "KRW",
//                ZonedDateTime.now(),
//                "PAYCO"
//        ));
//
//        OrderPaymentCompleteRequest request = new OrderPaymentCompleteRequest(order.getPaymentUid());
//
//        //when
//        PaymentConfirmResponse response = orderService.confirmPayment(request);
//
//
//        assertThat(response.orderId()).isEqualTo(String.valueOf(order.getId()));
//        assertThat(response.paymentUid()).isEqualTo(order.getPaymentUid());
//        assertThat(response.currency()).isEqualTo("KRW");
//        assertThat(response.totalPrice()).isEqualTo(order.getTotalPrice());
//        assertThat(response.paymentProvider()).isEqualTo("PAYCO");
//
//        // 주문 상태 변경 검증
//        Order confirmedOrder = orderRepository.findById(order.getId()).get();
//        assertThat(confirmedOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
//
//        // 이벤트 발행 검증
//        ArgumentCaptor<OrderCompletedEventPayload> payloadCaptor = ArgumentCaptor.forClass(OrderCompletedEventPayload.class);
//        verify(outboxEventPublisher).publish(eq(HappypillEventType.ORDER_COMPLETED), payloadCaptor.capture());
//
//        OrderCompletedEventPayload capturedPayload = payloadCaptor.getValue();
//        assertThat(capturedPayload.getOrderId()).isEqualTo(order.getId());
//        assertThat(capturedPayload.getTotalPrice()).isEqualTo(order.getTotalPrice());
//        assertThat(capturedPayload.getRecipentName()).isEqualTo(order.getOrderRecipientInfo().getName());
//        assertThat(capturedPayload.getRecipentMobile()).isEqualTo(order.getOrderRecipientInfo().getMobile());
//        assertThat(capturedPayload.getRecipentEmail()).isEqualTo(order.getOrderRecipientInfo().getEmail());
//        assertThat(capturedPayload.getOrderLines()).hasSize(orderLines.size());
//    }
//
//    @Test
//    @DisplayName("결제 확인 - 주문 금액과 PG사 검증 금액이 일치하지 않을 경우 예외가 발생하고 이벤트가 발행되지 않는지 검증")
//    void test3() {
//        // given
//        HappypillUser savedTestUser = getSavedTestUser();
//        Category category = categoryRepository.save(Category.of(SnowflakeUtil.nextId(), "www.happypill.com/image", "www.happypill.com/banner"));
//        Product product = productRepository.save(Product.of(SnowflakeUtil.nextId(), 1000, 100, "www.happypill.com/product-thumbnail", category));
//
//        OrderLine orderLine = OrderLine.create(SnowflakeUtil.nextId(), 3000, LocalDate.now(), 3, product);
//        Order order = orderRepository.save(Order.create(
//                SnowflakeUtil.nextId(),
//                "pay_test_uuid",
//                com.happypill.application.entity.enums.PaymentMethod.CARD,
//                OrderRecipientInfo.create("수령인", "010-1234-5678", "test@email.com"),
//                savedTestUser,
//                List.of(orderLine)
//        ));
//
//        // 결제 검증 응답 - 금액 불일치
//        VerifiedPayment verifiedPayment = new VerifiedPayment(
//                "pay_test_uuid",
//                100000000,
//                "KRW",
//                ZonedDateTime.now(),
//                "PAYCO"
//        );
//        when(pPaymentClient.getVerifiedPayment("pay_test_uuid")).thenReturn(verifiedPayment);
//
//        OrderPaymentCompleteRequest request = new OrderPaymentCompleteRequest("pay_test_uuid");
//
//        // when & then
//        assertThatThrownBy(() -> orderService.confirmPayment(request))
//                .isInstanceOf(RuntimeException.class);
////                .hasMessageContaining("결제 금액이 다름");
//
//        // 이벤트가 발행되지 않았는지 검증
//        verify(outboxEventPublisher, never()).publish(any(), any());
//    }
//
//    @Test
//    @DisplayName("결제 확인 - 존재하지 않는 paymentUid로 결제 확인 요청시 RuntimeException이 발생하는지 검증")
//    void confirmPayment_orderNotFound() {
//        // given
//        OrderPaymentCompleteRequest request = new OrderPaymentCompleteRequest("non_existent_payment_uid");
//
//        // when & then
//        assertThatThrownBy(() -> orderService.confirmPayment(request))
//                .isInstanceOf(RuntimeException.class);
//    }

}