package com.happypill.application.service.product.response;

import java.util.List;

public record CustomPageResponse<T> (List<T> products, boolean hasNext, Long lastProductId){}
