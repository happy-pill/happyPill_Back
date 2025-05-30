package com.happypill.application.exception.global;

public record ErrorResponse<T>(T message) {
}
