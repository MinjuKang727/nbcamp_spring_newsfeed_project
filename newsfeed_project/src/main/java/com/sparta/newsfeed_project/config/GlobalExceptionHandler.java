package com.sparta.newsfeed_project.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ApiResponse<?> getIllegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler
    public ApiResponse<?> getEtcErrorResponse(Exception e) {
        return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
