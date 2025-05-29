package com.happypill.application.exception;

import com.happypill.application.exception.custom.EmailException;
import com.happypill.application.service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    //이메일 전송 로직에서 예외 발생 시 예외처리
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ApiResponse<String>> emailException(EmailException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.of(e.getMessage()));
    }

    //@Valid @RequestBody 에서 유효하지 않을 경우 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> inValidRequestException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst() // 여러 에러 메시지 중 첫 번째만 사용
                .map(FieldError::getDefaultMessage)
                .orElse("잘못된 요청입니다.");

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.of(errorMessage));
    }
}
