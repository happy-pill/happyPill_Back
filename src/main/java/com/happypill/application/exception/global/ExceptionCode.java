package com.happypill.application.exception.global;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    USER_NOT_FOUND(404, "USER_001", "해당 유저를 찾을 수 없습니다."),
    USER_CAN_NOT_BE_NULL(400, "USER_002", "사용자는 null이 될 수 없습니다."),
    USER_ID_NOT_FOUND(404, "USER_003", "해당되는 id의 사용자를 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }
}