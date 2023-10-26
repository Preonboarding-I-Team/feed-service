package com.wanted.preonboarding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    ACCESS_TOKEN_EXPIRED("JX01", 401, "Access token expired!"),
    INVALID_TOKEN("JX02", 401, "Invalid Token!"),

    INFORMATION_NOT_MATCHED("AX01", 409, "Information not matched!"),
    LOGIN_REQUIRED_FIRST("AX02", 401, "Login required first"),

    USER_ALREADY_EXIST("UX01", 409, "User already exists!"),
    USER_NOT_FOUND("UX01", 404, "user not found");

    private final String errorCode;

    private final int status;

    private final String message;
}
