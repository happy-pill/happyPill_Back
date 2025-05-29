package com.happypill.application.exception.custom;

import com.happypill.application.exception.global.BaseException;
import com.happypill.application.exception.global.ExceptionCode;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(){
        super(ExceptionCode.USER_NOT_FOUND);
    }
}