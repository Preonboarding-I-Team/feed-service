package com.wanted.preonboarding.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.security.dto.UsernamePassword;
import com.wanted.preonboarding.security.jwt.JwtProperties;
import com.wanted.preonboarding.security.jwt.TokenProvider;
import com.wanted.preonboarding.security.repository.RedisRepository;
import com.wanted.preonboarding.security.user.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;

    private final JwtProperties jwtProperties;

    private final RedisRepository redisRepository;

    private final ObjectMapper objectMapper;


    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePassword usernamePassword = objectMapper.readValue(request.getInputStream(), UsernamePassword.class);
        var authentication = new UsernamePasswordAuthenticationToken(usernamePassword.getUsername(), usernamePassword.getPassword());
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(principal.getUsername(), principal.getId(), principal.getEmail());
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        String refreshToken = tokenProvider.generateRefreshToken(principal.getUsername());
        redisRepository.saveRefreshToken(refreshToken, principal.getUsername());
        Cookie cookie = createCookie(refreshToken);
        response.addCookie(cookie);
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("Refresh", value);
        cookie.setDomain("localhost");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtProperties.getRefreshTokenExpirationMinutes() * 60);
        cookie.setPath("/reissue");
        cookie.setSecure(true);
        return cookie;
    }
}
