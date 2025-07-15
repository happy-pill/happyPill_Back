package com.happypill.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidNicknameValidator implements ConstraintValidator<ValidNickname, String> {

    private static final String FORBIDDEN_WORDS = "null";

    @Override
    public boolean isValid(String nickName, ConstraintValidatorContext constraintValidatorContext) {
        if(nickName == null) {
            return true; //@NotBlank 에서 검사하므로 여기에선 true 반환(@NotBlank 가 처리하도록 위임)
        }
        return !nickName.trim().equalsIgnoreCase(FORBIDDEN_WORDS);
    }
}
