package com.happypill.application.service.order.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.List;

public record OrderCreateRequest(
        String recipentName,
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
