package com.sparta.newsfeed_project.config;

import com.sparta.newsfeed_project.domain.common.ApiResponse;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ApiResponse.createError("Validation Exception", e.getBindingResult().getAllErrors().toString(), HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException e) {
        return ApiResponse.createError("제약 조건 위반", e.getConstraintViolations().toString(), HttpStatus.BAD_REQUEST.value());
    }
}
