package com.sparta.newsfeed_project.domain.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final String message;
    private final String detailMessage;
    private final Integer statusCode;
    private final T data;

    private ApiResponse(String message, String detailMessage, Integer statusCode, T data) {
        this.message = message;
        this.detailMessage = detailMessage;
        this.statusCode = statusCode;
        this.data = data;
    }

    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> createSuccess(String message, Integer statusCode, T data) {
        return new ApiResponse<>(message, null, statusCode, data);
    }

    /**
     * 에러 응답 생성
     */
    public static <T> ApiResponse<T> createError(String message, String detailMessage, Integer statusCode) {
        return new ApiResponse<>(message, detailMessage, statusCode, null);
    }
}
