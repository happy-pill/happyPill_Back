package com.happypill.application.service.order;

import com.happypill.application.auth.UserContext;
import com.happypill.application.entity.*;
import com.happypill.application.entity.enums.PaymentMethod;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.repository.order.OrderRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.service.order.payment.PPaymentClient;
import com.happypill.application.service.order.payment.VerifiedPayment;
import com.happypill.application.service.order.request.OrderCreateRequest;
import com.happypill.application.service.order.request.OrderPaymentCompleteRequest;
import com.happypill.application.service.order.response.OrderResponse;
import com.happypill.application.service.order.response.PaymentConfirmResponse;
import com.happypill.application.util.SnowflakeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final HappypillUserRepository userRepository;

    private final PPaymentClient pPaymentClient;

    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    public OrderResponse createOrder(UserContext userContext, OrderCreateRequest request) {
        Map<Long, Product> productMap = getOrderProductMap(request);
        List<OrderLine> orderLines = decreaseStockAndGetOrderLines(request, productMap);

        HappypillUser orderedUser = userRepository.findById(userContext.getId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        OrderRecipientInfo orderRecipientInfo = OrderRecipientInfo.create(request.recipentName(), request.recipentMobile(), orderedUser.getNotifyEmail());

        Order order = orderRepository.save(Order.create(
                SnowflakeUtil.nextId(),
                generatePaymentUId(),
                PaymentMethod.CARD,
                orderRecipientInfo,
                orderedUser,
                orderLines
        ));

        return OrderResponse.from(order);
    }

    private List<OrderLine> decreaseStockAndGetOrderLines(OrderCreateRequest request, Map<Long, Product> ProductMap) {
        List<OrderLine> orderLines = new ArrayList<>();
        for (var o : request.orderLineCreateRequests()) {
            Product orderProduct = ProductMap.get(o.productId());

            orderProduct.decreaseStock(o.month());

            OrderLine orderLine = OrderLine.create(
                    SnowflakeUtil.nextId(),
                    calculateOrderLinePriceWithMonth(o.month(), orderProduct.getPrice()),
                    o.startDate(),
                    o.month(),
                    orderProduct
            );
            orderLines.add(orderLine);
        }
        return orderLines;
    }

    private Map<Long, Product> getOrderProductMap(OrderCreateRequest request) {
        List<Long> productIds = request.orderLineCreateRequests().stream()
                .map(OrderCreateRequest.OrderLineCreateRequest::productId)
                .toList();
        Map<Long, Product> productMap = productRepository.findAllByIdIn(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        if (productIds.size() != productMap.size()) {
            throw new RuntimeException("주문목록 정보가 잘못되었습니다");
        }
        return productMap;
    }

    private String generatePaymentUId() {
        return "pay_" + UUID.randomUUID();
    }

    private int calculateOrderLinePriceWithMonth(int month, int price) {
        return month * price;
    }

    @Transactional
    public PaymentConfirmResponse confirmPayment(OrderPaymentCompleteRequest request) {
        String paymentUid = request.paymentUid();
        VerifiedPayment verifiedPayment = pPaymentClient.getVerifiedPayment(paymentUid);

        Order order = orderRepository.findByPaymentUid(paymentUid)
                .orElseThrow(() -> new RuntimeException("해당 주문이 없습니다"));

        if (order.getTotalPrice() != verifiedPayment.totalAmount()) { //long
            throw new RuntimeException("결제 금액이 다름 orderId = %s, totalPrice=%s, verifiedPayment=%s"
                    .formatted(order.getId(), order.getTotalPrice(), verifiedPayment)
            );
        }

        order.complete();

        return new PaymentConfirmResponse(
                String.valueOf(order.getId()),
                order.getPaymentUid(),
                verifiedPayment.currency(),
                order.getTotalPrice(),
                verifiedPayment.paidAt(),
                verifiedPayment.paymentProvider()
        );
    }

}
