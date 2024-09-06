package com.sparta.newsfeed_project.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.LikeException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("에러 메세지 JSON 형식 변환 오류");
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<String> handleCommonException(CommonException e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(Map.of(
                e.getMessage(), e.getCause().getMessage()
        ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(LikeException.class)
    public ResponseEntity<String> handleLikeException(LikeException e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> errorMap = Map.of("message", e.getMessage());
        return ResponseEntity.status(403).body(objectMapper.writeValueAsString(errorMap));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationServiceException.class)
    public ResponseEntity<String> handleAuthorizationServiceException(AuthorizationServiceException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> errorMap = Map.of("message", "Validation Exception");
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        String message = objectMapper.writeValueAsString(errorMap);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> errorMap = Map.of("message", "Violate Constraint");
        e.getConstraintViolations().forEach(error ->
                errorMap.put(error.getInvalidValue().toString(), error.getMessage()));
        String message = objectMapper.writeValueAsString(errorMap);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> errorMap = Map.of("message", "토큰 오류", "error", e.getMessage());
        String message = objectMapper.writeValueAsString(errorMap);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
