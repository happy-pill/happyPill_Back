package com.happypill.api.controller.order;

import com.happypill.api.config.auth.usercontext.HappypillUser;
import com.happypill.application.auth.UserContext;
import com.happypill.application.service.order.OrderService;
import com.happypill.application.service.order.request.OrderCreateRequest;
import com.happypill.application.service.order.request.OrderPaymentCompleteRequest;
import com.happypill.application.service.order.response.OrderResponse;
import com.happypill.application.service.order.response.PaymentConfirmResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문", description = "사용자의 주문 및 결제 처리를 위한 API")
@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "사용자가 상품을 주문하는 API")
    @PostMapping("/api/order")
    public OrderResponse createOrder(@HappypillUser UserContext userContext, @RequestBody @Valid OrderCreateRequest request) {

        return orderService.createOrder(userContext, request);
    }

    @Operation(summary = "결제 완료 확인", description = "결제 완료 후 결제 정보를 검증하고 주문 상태를 확정하는 API")
    @PostMapping("/api/order/payment-confirm")
    public PaymentConfirmResponse confirmPayment(@HappypillUser UserContext userContext, @RequestBody @Valid OrderPaymentCompleteRequest request) {
        return orderService.confirmPayment(request);
    }

}
