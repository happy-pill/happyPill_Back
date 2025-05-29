package com.happypill.application.exception.global;

public record ErrorResponse(int status, String code, String message) {
}
