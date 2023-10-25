package com.wanted.preonboarding.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.security.filter.JwtAuthenticationFilter;
import com.wanted.preonboarding.security.filter.JwtVerificationFilter;
import com.wanted.preonboarding.security.jwt.JwtProperties;
import com.wanted.preonboarding.security.jwt.TokenProvider;
import com.wanted.preonboarding.security.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtFilterDsl extends AbstractHttpConfigurer<JwtFilterDsl, HttpSecurity> {

    private final TokenProvider tokenProvider;

    private final JwtProperties jwtProperties;

    private final AuthenticationFailureHandler customAuthenticationFailureHandler;

    private final RedisRepository redisRepository;

    private final ObjectMapper objectMapper;

    @Override
    public void configure(HttpSecurity security) {
        AuthenticationManager authenticationManager = security.getSharedObject(AuthenticationManager.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider, jwtProperties, redisRepository, objectMapper);
        jwtAuthenticationFilter.setFilterProcessesUrl("/sign-in");
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        security.addFilter(jwtAuthenticationFilter);

        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(tokenProvider);
        security.addFilterBefore(jwtVerificationFilter, JwtAuthenticationFilter.class);
    }
}
