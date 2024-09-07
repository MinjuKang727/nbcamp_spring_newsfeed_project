package com.sparta.newsfeed_project.domain.common.exception;

import lombok.Getter;

@Getter
public class CommonException extends Exception {
    private ExceptionCode code;
    private Exception cause;
    private String message;

    public CommonException(ExceptionCode code) {
        this.code = code;
        this.message =code.getMessage();
    }

    private CommonException withCause(Exception cause) {
        this.cause = cause;
        this.message = cause.getMessage();
        return this;
    }
}
