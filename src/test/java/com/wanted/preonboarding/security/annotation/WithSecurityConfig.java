package com.wanted.preonboarding.security.annotation;

import com.wanted.preonboarding.security.config.JwtFilterDsl;
import com.wanted.preonboarding.security.config.PasswordEncoderConfig;
import com.wanted.preonboarding.security.config.SecurityConfig;
import com.wanted.preonboarding.security.handler.CustomAuthenticationEntryPointHandler;
import com.wanted.preonboarding.security.handler.CustomAuthenticationFailureHandler;
import com.wanted.preonboarding.security.handler.CustomStatusLogoutSuccessHandler;
import com.wanted.preonboarding.security.jwt.JwtProperties;
import com.wanted.preonboarding.security.jwt.TokenProvider;
import com.wanted.preonboarding.security.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({SecurityConfig.class,
        JwtFilterDsl.class,
        TokenProvider.class,
        JwtProperties.class,
        CustomAuthenticationFailureHandler.class,
        CustomAuthenticationEntryPointHandler.class,
        CustomStatusLogoutSuccessHandler.class,
        PasswordEncoderConfig.class,
        UserDetailsServiceImpl.class
})
public @interface WithSecurityConfig {
}
