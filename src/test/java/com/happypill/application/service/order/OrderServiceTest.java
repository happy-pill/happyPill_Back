package com.happypill.application.service.order;

import com.happypill.api.config.auth.usercontext.SecurityUserContext;
import com.happypill.application.entity.*;
import com.happypill.application.entity.enums.OrderStatus;
import com.happypill.application.entity.enums.Provider;
import com.happypill.application.entity.enums.Role;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.repository.order.OrderRepository;
import com.happypill.application.repository.product.ProductRepository;
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

    private HappypillUser getSavedTestUser() {
        return userRepository.save(HappypillUser.ofSocial(
                SnowflakeUtil.nextId(),
                faker.name().toString(),
                Provider.GOOGLE,
                faker.number().digits(6),
                "test@gmail.com",
                "test@gmail.com",
                Role.USER)
        );
    }

    @Test
    @DisplayName("success")
    void test1() {
        HappypillUser savedTestUser = getSavedTestUser();
        SecurityUserContext userContext = SecurityUserContext.from(savedTestUser.getId());
        Category category = categoryRepository.save(Category.of(SnowflakeUtil.nextId(), "www.happypill.com/image", "www.happypill.com/banner"));
        Product product1 = productRepository.save(Product.of(SnowflakeUtil.nextId(), 1000, 100, "www.happypill.com/product1-thumbnail", category));
        Product product2 = productRepository.save(Product.of(SnowflakeUtil.nextId(), 2000, 200, "www.happypill.com/product2-thumbnail", category));
        var orderLineCreateRequest1 = new OrderCreateRequest.OrderLineCreateRequest(product1.getId(), 3, LocalDate.of(2026, 1, 1));
        var orderLineCreateRequest2 = new OrderCreateRequest.OrderLineCreateRequest(product2.getId(), 1, LocalDate.of(2026, 2, 2));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest("recipentName", "recipentMobile",
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

}