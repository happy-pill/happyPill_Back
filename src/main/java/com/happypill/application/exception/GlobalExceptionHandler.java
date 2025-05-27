package com.happypill.application.exception;

import com.happypill.application.service.dto.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //이메일 전송 로직에서 예외 발생 시 예외처리
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ApiResponse> emailException(EmailException e) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json"));
        ApiResponse apiResponse = new ApiResponse(e.getMessage());
        return new ResponseEntity<>(apiResponse, httpHeaders, e.getStatus());
    }

    //@Valid @RequestBody 에서 유효하지 않을 경우 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> inValidRequestException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst() //여러 에러메시지 발생 첫 번째 에러메시지만 가져옴
                .map(FieldError::getDefaultMessage)
                .orElse("잘못된 요청입니다.");
        ApiResponse apiResponse = new ApiResponse(errorMessage);
        return ResponseEntity.badRequest().body(apiResponse);
    }
}