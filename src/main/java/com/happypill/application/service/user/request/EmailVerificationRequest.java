package com.happypill.application.service.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record EmailVerificationRequest(
        @Email
        @NotNull
        String notifyEmail
) {
}
