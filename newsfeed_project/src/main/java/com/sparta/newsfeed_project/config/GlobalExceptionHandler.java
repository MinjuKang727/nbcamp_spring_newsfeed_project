package com.sparta.newsfeed_project.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeed_project.domain.common.exception.LikeException;
import com.sparta.newsfeed_project.domain.user.UserException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Json 형식 변환 Exception handler
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<JsonProcessingException> handleJsonProcessingException(JsonProcessingException e) {
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String,String>> handleUserException(UserException e) {
        Map<String, String> errorMap = Map.of(e.getMessage(), e.getCause().getMessage());
        if (e.getCause().getClass() == NoSuchElementException.class) {
            return new ResponseEntity<>(errorMap, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = Map.of("message", "Validation Exception");
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errorMap = Map.of("message", "Violate Constraint", "error", e.toString());

        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleIOException(IOException e) {
        Map<String, String> errorMap = Map.of("message", "Token Error", "error", e.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}
