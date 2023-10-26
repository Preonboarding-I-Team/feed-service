package com.wanted.preonboarding.dto;

import com.wanted.preonboarding.exception.ExceptionCode;
import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Set;

@Getter
public class ErrorResponse {

    private String errorCode;

    private int status;

    private String message;

    private List<FieldError> fieldErrors;

    private List<ConstraintViolationError> violationErrors;

    private ErrorResponse(String errorCode, int status, String message) {
        this.errorCode = errorCode;
        this.status = status;
        this.message = message;
    }

    private ErrorResponse(List<FieldError> fieldErrors, List<ConstraintViolationError> violationErrors) {
        this.fieldErrors = fieldErrors;
        this.violationErrors = violationErrors;
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(FieldError.of(bindingResult), null);
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
        return new ErrorResponse(null, ConstraintViolationError.of(violations));
    }

    public static ErrorResponse of(ExceptionCode exceptionCode) {
        return new ErrorResponse(exceptionCode.getErrorCode(), exceptionCode.getStatus(), exceptionCode.getMessage());
    }

    public static ErrorResponse of(HttpStatus httpStatus) {
        return new ErrorResponse(null, httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(null, httpStatus.value(), message);
    }

    @Getter
    @AllArgsConstructor
    public static class FieldError {

        private final String field;

        private final Object rejectedValue;

        private final String reason;

        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()
                    )).toList();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ConstraintViolationError {
        private final String propertyPath;

        private final Object rejectedValue;

        private final String reason;

        public static List<ConstraintViolationError> of(Set<ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(constraintViolation -> new ConstraintViolationError(
                            constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getInvalidValue().toString(),
                            constraintViolation.getMessage()
                    )).toList();
        }
    }
}
