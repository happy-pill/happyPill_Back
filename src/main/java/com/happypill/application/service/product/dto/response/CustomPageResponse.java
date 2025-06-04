package com.happypill.application.service.product.dto.response;

import java.util.List;

public record CustomPageResponse<T> (List<T> products, boolean hasNext, Long lastProductId){}
