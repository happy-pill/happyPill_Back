package com.happypill.application.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailException extends RuntimeException{
    private final HttpStatus status;
    public EmailException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
}
