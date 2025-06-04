package com.happypill.application.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ExceptionCode {
    // 사용자
    USER_NOT_FOUND(NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // 상품
    PRODUCT_NOT_FOUND(NOT_FOUND, "해당 상품을 찾을 수 없습니다."),

    // 상품 정보
    PRODUCT_INFO_NOT_FOUND(NOT_FOUND, "해당 상품 정보를 찾을 수 없습니다."),

    // 상품 가격
    PRODUCT_PRICE_NOT_FOUND(NOT_FOUND, "해당 상품 가격을 찾을 수 없습니다."),

    // 카테고리
    CATEGORY_NOT_FOUND(NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),

    // 이메일
    EMAIL_REQUEST_COUNT_EXCEED(TOO_MANY_REQUESTS, "요청 기능 횟수를 초과했습니다"),
    EMAIL_VERIFICATION_CODE_NOT_FOUND(BAD_REQUEST, "인증 코드가 만료되었거나 존재하지 않습니다");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }
}