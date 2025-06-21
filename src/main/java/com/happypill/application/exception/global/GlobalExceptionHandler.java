package com.happypill.application.exception.global;

import com.happypill.application.exception.custom.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // @Valid 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("잘못된 요청입니다.");

        ErrorResponse<Object> response = new ErrorResponse<>(message, null);
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse<Object>> handleBusinessException(BusinessException e) {
        ExceptionCode code = e.getExceptionCode();

        ErrorResponse<Object> response = new ErrorResponse<>(
                code.getMessage(),
                e.getContext()
        );

        return ResponseEntity.status(code.getStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse<?>> handleUnexpectedException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse<>("예상치 못한 오류가 발생했습니다.", e));  //개발단계에서만
//        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse<>("예상치 못한 오류가 발생했습니다.", null)); //TODO
    }
}