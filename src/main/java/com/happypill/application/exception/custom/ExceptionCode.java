package com.happypill.application.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ExceptionCode {

    USER_NOT_FOUND(NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    USER_CAN_NOT_BE_NULL(BAD_REQUEST,"사용자는 null이 될 수 없습니다."),
    USER_ID_NOT_FOUND(NOT_FOUND ,"해당되는 id의 사용자를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }
}