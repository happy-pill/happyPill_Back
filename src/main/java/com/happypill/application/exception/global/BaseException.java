package com.happypill.application.exception.global;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{

    private final ExceptionCode exceptionCode;

    public BaseException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}