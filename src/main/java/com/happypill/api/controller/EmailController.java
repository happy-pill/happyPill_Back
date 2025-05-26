package com.happypill.api.controller;

import com.happypill.application.service.dto.EmailRequestLimitService;
import com.happypill.application.service.dto.EmailService;
import com.happypill.application.service.dto.request.EmailVerifyConfirmRequest;
import com.happypill.application.service.dto.request.EmailVerifyRequest;
import com.happypill.application.service.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/verification-request")
    public ResponseEntity<?> requestEmailCode(@Valid @RequestBody EmailVerifyRequest request
                                              @AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal){
        if(oAuth2UserPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("로그인이 필요합니다."));
        emailService.sendEmail("cdh3946@gmail.com", request.newEmail());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("인증코드가 전송되었습니다."));
    }
    @PostMapping("/verification-confirm")
    public ResponseEntity<?> confirmEmailCode(@Valid @RequestBody EmailVerifyConfirmRequest request){
        emailService.confirmCode(request.newEmail(), request.code());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("인증 완료"));
    }
}
