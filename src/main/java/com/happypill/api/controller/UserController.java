package com.happypill.api.controller;

import com.happypill.api.config.auth.usercontext.HappypillUser;
import com.happypill.application.auth.UserContext;
import com.happypill.application.exception.global.ErrorResponse;
import com.happypill.application.service.user.UserService;
import com.happypill.application.service.user.request.EmailVerificationRequest;
import com.happypill.application.service.user.request.UserNotifyEmailUpdateRequest;
import com.happypill.application.service.user.request.UserUpdateRequest;
import com.happypill.application.service.user.response.UserInfoResponse;
import com.happypill.application.swagger.AuthFailureResponses;
import com.happypill.application.swagger.OKAndServerErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자", description = "사용자 정보 및 이메일 인증 관련 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "특정 회원정보 조회", description = "회원정보 수정 시 회원정보가 출력되도록 하기 위한 API")
    @AuthFailureResponses
    @OKAndServerErrorResponses
    @ApiResponse(responseCode = "404", description = "회원정보가 존재하지 않을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/api/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserInfoResponse getCurrentUser(@HappypillUser UserContext userContext) {
        return userService.getUserInfo(userContext);
    }

    @Operation(summary = "회원정보 수정", description = "회원 정보를 수정하기 위한 API")
    @AuthFailureResponses
    @OKAndServerErrorResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 DTO의 유효성 검증에 실패했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원정보가 존재하지 않을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/api/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserInfoResponse updateCurrentUser(@HappypillUser UserContext userContext, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return userService.updateUser(userContext, userUpdateRequest);
    }

    @Operation(summary = "회원탈퇴", description = "사용자가 자신의 계정을 삭제하기 위한 API")
    @AuthFailureResponses
    @OKAndServerErrorResponses
    @DeleteMapping("/api/user/me")
    @PreAuthorize("hasRole('USER')")
    public void deleteCurrentUser() {
        throw new AssertionError("not implemented yet");
    }

    @Operation(summary = "이메일 인증번호 전송요청", description = "이메일 인증번호를 요청하기 위한 API")
    @AuthFailureResponses
    @OKAndServerErrorResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 DTO의 유효성 검증에 실패했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "429", description = "인증번호 요청 가능 횟수를 초과한 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/user/me/email/verification-request")
    @PreAuthorize("hasRole('USER')")
    public void sendNotifyEmailVerificationCode(@HappypillUser UserContext userContext, @RequestBody @Valid EmailVerificationRequest request) {
        userService.sendEmailVerificationCode(userContext, request);
    }

    @Operation(summary = "이메일 인증번호 검증", description = "이메일 인증번호를 검증하기 위한 API")
    @AuthFailureResponses
    @OKAndServerErrorResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 DTO의 유효성 검증에 실패했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "제출한 인증 코드가 만료되었거나 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원정보가 존재하지 않을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/user/me/email/verification-confirm")
    @PreAuthorize("hasRole('USER')")
    public void verifyAndUpdateNotifyEmail(@HappypillUser UserContext userContext,
                                           @RequestBody @Valid UserNotifyEmailUpdateRequest request) {
        userService.verifyAndUpdateUserNotifyEmail(userContext, request);
    }
}
