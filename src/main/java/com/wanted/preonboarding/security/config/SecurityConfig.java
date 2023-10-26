package com.wanted.preonboarding.security.config;

import com.wanted.preonboarding.security.handler.CustomAuthenticationEntryPointHandler;
import com.wanted.preonboarding.security.handler.CustomStatusLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilterDsl jwtFilterDsl;

    private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;

    private final CustomStatusLogoutSuccessHandler customStatusLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.apply(jwtFilterDsl);

        return http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .headers(header -> header
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutSuccessHandler(customStatusLogoutSuccessHandler)
                        .logoutUrl("/logout")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPointHandler))
                .build();
    }

}
