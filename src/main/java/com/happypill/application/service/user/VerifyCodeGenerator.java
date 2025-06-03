package com.happypill.application.service.user;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class VerifyCodeGenerator {
    private final Random random = new SecureRandom();

    public String generate() {
        return String.valueOf(random.nextInt(900000) + 100000);
    }
}
