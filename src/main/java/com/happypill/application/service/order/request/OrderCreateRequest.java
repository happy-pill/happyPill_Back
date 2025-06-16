package com.happypill.application.service.order.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.List;

public record OrderCreateRequest(

        @NotBlank(message = "수신인 이름은 필수입니다")
        String recipentName,

        @NotBlank(message = "수신인 전화번호는 필수입니다")
        @Pattern(regexp = "^010\\d{7,8}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
        String recipentMobile,

        @NotEmpty
        @Size(min = 1, max = 10)
        @Valid
        List<OrderLineCreateRequest> orderLineCreateRequests
) {
    public record OrderLineCreateRequest(

            @NotNull
            Long productId,

            @Range(min = 1, max = 3)
            @NotNull
            Integer month,

            @NotNull
            @Future
            LocalDate startDate
    ) {
    }

}
