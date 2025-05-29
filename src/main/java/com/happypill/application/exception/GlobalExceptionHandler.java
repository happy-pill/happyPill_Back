package com.happypill.application.exception;

import com.happypill.application.exception.custom.EmailException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 이메일 전송 로직에서 예외 발생 시
    @ExceptionHandler(EmailException.class)
    public ProblemDetail handleEmailException(EmailException e) {
        ProblemDetail problem = ProblemDetail.forStatus(e.getStatus());
        problem.setTitle("Email Error");
        problem.setDetail(e.getMessage());
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("errorCode", "EMAIL_ERROR");
        return problem;
    }

    // @Valid 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("잘못된 요청입니다.");

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Failed");
        problem.setDetail(message);
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("errorCode", "VALIDATION_ERROR");
        return problem;
    }

    // 존재하지 않는 엔티티 조회 시 예외처리
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Entity Not Found");
        problem.setDetail(e.getMessage());
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("errorCode", "ENTITY_NOT_FOUND");
        return problem;
    }
}
