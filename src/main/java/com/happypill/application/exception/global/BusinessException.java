package com.happypill.application.exception.global;

import com.happypill.application.exception.custom.ExceptionCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ExceptionCode exceptionCode;
    private final Object context;

    public BusinessException(ExceptionCode exceptionCode) {
        this(exceptionCode, null);
    }
    public BusinessException(ExceptionCode exceptionCode, Object context) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.context = context;
    }
}