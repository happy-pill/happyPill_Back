package com.happypill.application.service.order;

import com.happypill.application.auth.UserContext;
import com.happypill.application.entity.*;
import com.happypill.application.entity.enums.PaymentMethod;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.repository.order.OrderRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.service.order.payment.PortonePaymentClient;
import com.happypill.application.service.order.request.OrderCreateRequest;
import com.happypill.application.service.order.request.OrderPaymentCompleteRequest;
import com.happypill.application.service.order.response.OrderResponse;
import com.happypill.application.util.SnowflakeUtil;
import io.portone.sdk.server.payment.PaidPayment;
import io.portone.sdk.server.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final PortonePaymentClient portonePaymentClient;

    @Transactional
    public OrderResponse createOrder(UserContext userContext, OrderCreateRequest request) {
        Map<Long, Product> ProductMap = getProductMapWithLock(request);
        List<OrderLine> orderLines = decreaseStockAndGetOrderLines(request, ProductMap);

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
            if (o.month() > orderProduct.getStock()) {
                throw new RuntimeException("재고불가");
            }
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

    private Map<Long, Product> getProductMapWithLock(OrderCreateRequest request) {
        List<Long> productIds = request.orderLineCreateRequests().stream()
                .map(OrderCreateRequest.OrderLineCreateRequest::productId)
                .toList();
        Map<Long, Product> ProductMap = productRepository.findAllByProductIdsWithPessimisticLock(productIds).stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));
        if (productIds.size() != ProductMap.size()) {
            throw new RuntimeException("유효하지 않은 요청");
        }
        return ProductMap;
    }

    private String generatePaymentUId() {
        return "pay_" + UUID.randomUUID();
    }

    private int calculateOrderLinePriceWithMonth(int month, int price) {
        return month * price;
    }

    @Transactional
    public void confirmPayment(OrderPaymentCompleteRequest request) {
        String paymentUid = request.paymentUid();
        PaidPayment paidPayment = getPayment(paymentUid);

        Order order = orderRepository.findByPaymentUid(paymentUid)
                .orElseThrow(() -> new RuntimeException("해당 주문이 없습니다"));

        if (order.getTotalPrice() != paidPayment.getAmount().getTotal()) { //long
            throw new RuntimeException("결제 금액이 다름 orderId = %s, totalPrice=%s, paymentPrice=%s"
                    .formatted(order.getOrderId(), order.getTotalPrice(), paidPayment.getAmount().getTotal())
            );
        }

        order.complete();

    }

    PaidPayment getPayment(String paymentUid) {
        Payment rawpayment = portonePaymentClient.getPaymentInfo(paymentUid);
        if (rawpayment instanceof PaidPayment paidPayment) {
            return paidPayment;
        } else {
            throw new RuntimeException("paidPayment Error %s".formatted(rawpayment));
        }
    }
}
