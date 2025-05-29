package com.happypill.application.service.dto.response;

public record ApiResponse<T>(T message) {
    public static <T> ApiResponse<T> of(T data){
        return new ApiResponse<>(data);
    }
}
