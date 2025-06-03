package com.happypill.api.controller;

import com.happypill.api.config.auth.usercontext.HappypillUser;
import com.happypill.application.auth.UserContext;
import com.happypill.application.service.user.UserService;
import com.happypill.application.service.user.request.EmailVerificationRequest;
import com.happypill.application.service.user.request.UserNotifyEmailUpdateRequest;
import com.happypill.application.service.user.request.UserUpdateRequest;
import com.happypill.application.service.user.response.UserInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserInfoResponse getCurrentUser(@HappypillUser UserContext userContext) {
        return userService.getUserInfo(userContext);
    }

    @PatchMapping("/api/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserInfoResponse updateCurrentUser(@HappypillUser UserContext userContext, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return userService.updateUser(userContext, userUpdateRequest);
    }

    @DeleteMapping("/api/user/me")
    @PreAuthorize("hasRole('USER')")
    public void deleteCurrentUser() {
        throw new AssertionError("not implemented yet");
    }

    @PostMapping("/api/user/me/email/verification-request")
    @PreAuthorize("hasRole('USER')")
    public void sendNotifyEmailVerificationCode(@HappypillUser UserContext userContext, @RequestBody @Valid EmailVerificationRequest request) {
        userService.sendEmailVerificationCode(userContext, request);
    }

    @PostMapping("/api/user/me/email/verification-confirm")
    @PreAuthorize("hasRole('USER')")
    public void verifyAndUpdateNotifyEmail(@HappypillUser UserContext userContext,
                                           @RequestBody @Valid UserNotifyEmailUpdateRequest request) {
        userService.verifyAndUpdateUserNotifyEmail(userContext, request);
    }
}
