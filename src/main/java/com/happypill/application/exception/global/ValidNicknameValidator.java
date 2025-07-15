package com.happypill.application.exception.global;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class ValidNicknameValidator implements ConstraintValidator<ValidNickname, String> {

    private static final String FORBIDDEN_WORDS = "null";

    @Override
    public boolean isValid(String nickName, ConstraintValidatorContext constraintValidatorContext) {
        return !FORBIDDEN_WORDS.contains(nickName.trim().toLowerCase());
    }
}
