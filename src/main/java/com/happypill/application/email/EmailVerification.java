package com.happypill.application.email;

public interface EmailVerification {

    String generateCode(String email);

    String buildEmailContent(String code);

    boolean confirmCode(String email, String code);
}
