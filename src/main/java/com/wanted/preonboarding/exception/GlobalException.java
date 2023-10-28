package com.wanted.preonboarding.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public GlobalException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
