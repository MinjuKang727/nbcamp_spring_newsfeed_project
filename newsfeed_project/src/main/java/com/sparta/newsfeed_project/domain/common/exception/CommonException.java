package com.sparta.newsfeed_project.domain.common.exception;

import java.io.IOException;

public class CommonException extends Exception {
    private ExceptionCode code;
    private Throwable e;


    public CommonException(ExceptionCode code, Throwable e) {
        super(code.getCode(), e);
    }

    public CommonException(ExceptionCode code) {
        super(code.getCode());
    }

    public CommonException(String message) {
        super(message);
    }
}
