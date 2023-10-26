package com.wanted.preonboarding.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.dto.ErrorResponse;
import com.wanted.preonboarding.exception.ExceptionCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object e = request.getAttribute("exception");
        if (!isExceptionCode(e)) {
            responseWithJson(response, ExceptionCode.LOGIN_REQUIRED_FIRST);
            return;
        }
        ExceptionCode exceptionCode = convertToExceptionCode(e);
        responseWithJson(response, exceptionCode);
    }

    private boolean isExceptionCode(Object exception) {
        return exception instanceof ExceptionCode;
    }

    private ExceptionCode convertToExceptionCode(Object object) {
        return (ExceptionCode) object;
    }

    private void responseWithJson(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(exceptionCode.getStatus());
        response.getWriter().write(toJsonResponse(exceptionCode));
    }

    private String toJsonResponse(ExceptionCode exceptionCode) throws JsonProcessingException {
        ErrorResponse errorResponse = ErrorResponse.of(exceptionCode);
        return objectMapper.writeValueAsString(errorResponse);
    }
}
