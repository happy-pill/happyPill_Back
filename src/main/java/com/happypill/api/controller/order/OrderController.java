package com.happypill.api.controller.order;

import com.happypill.api.config.auth.usercontext.HappypillUser;
import com.happypill.application.auth.UserContext;
import com.happypill.application.service.order.OrderService;
import com.happypill.application.service.order.request.OrderCreateRequest;
import com.happypill.application.service.order.request.OrderPaymentCompleteRequest;
import com.happypill.application.service.order.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/order")
    public OrderResponse createOrder(@HappypillUser UserContext userContext, @RequestBody @Valid OrderCreateRequest request) {

        return orderService.createOrder(userContext, request);
    }

    @PostMapping("/api/order/payment-confirm")
    public void confirmPayment(@HappypillUser UserContext userContext, @RequestBody @Valid OrderPaymentCompleteRequest request) {
        orderService.confirmPayment(request);
    }

}
