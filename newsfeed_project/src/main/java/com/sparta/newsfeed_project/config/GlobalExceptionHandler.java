package com.sparta.newsfeed_project.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.newsfeed_project.domain.common.ApiResponse;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CommonException.class)
    public ApiResponse<Void> handleCommonException(CommonException e) {
        return ApiResponse.createError(e.getMessage(), e.getCause().getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonProcessingException.class)
    public ApiResponse<Void> handleJsonProcessingException(JsonProcessingException e) {
        return ApiResponse.createError("회원 가입 실패", "Validation Exception", HttpStatus.BAD_REQUEST.value());
    }
}
