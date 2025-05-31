package com.happypill.application.exception.global;

public record ErrorResponse<T>(String message, T data) {
}