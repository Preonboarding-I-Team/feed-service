package com.wanted.preonboarding.security.service;

import com.wanted.preonboarding.security.jwt.JwtProperties;
import com.wanted.preonboarding.security.jwt.TokenProvider;
import com.wanted.preonboarding.security.repository.RedisRepository;
import com.wanted.preonboarding.user.entity.User;
import com.wanted.preonboarding.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final JwtProperties jwtProperties;
    
    private final TokenProvider tokenProvider;
    
    private final UserRepository userRepository;

    private final RedisRepository redisRepository;
    
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        User user = verifyRefreshTokenExists(request);

        String newAccessToken = tokenProvider.generateAccessToken(user.getAccount(), user.getId(), user.getEmail());
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);

        String newRefreshToken = tokenProvider.generateRefreshToken(user.getAccount());
        Cookie cookie = createCookie(newRefreshToken);
        redisRepository.saveRefreshToken(newRefreshToken, user.getAccount());
        response.addCookie(cookie);
    }
    
    private User verifyRefreshTokenExists(HttpServletRequest request) {
        Cookie[] verifiedCookies = verifyCookiesAreNull(request);
        Cookie cookie = verifyRefreshTokenExists(verifiedCookies);
        String cookieRefreshToken = cookie.getValue();
        User user = verifyRefreshTokenSubject(cookieRefreshToken);
        verifyRedis(user, cookieRefreshToken);

        redisRepository.deleteRefreshToken(cookieRefreshToken);
        return user;
    }

    private void verifyRedis(User user, String cookieRefreshToken) {
        String subject = Optional.ofNullable(redisRepository.getSubject(cookieRefreshToken))
                .orElseThrow(() -> new RuntimeException("RefreshToken not found in redis!"));
        if (!user.getAccount().equals(subject)) {
            throw new RuntimeException("RefreshToken Subject is not matched!");
        }
    }

    private User verifyRefreshTokenSubject(String cookieRefreshToken) {
        String account = tokenProvider.getClaims(cookieRefreshToken).getSubject();
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("RefreshToken not found in Database!"));
    }

    private Cookie verifyRefreshTokenExists(Cookie[] verifiedCookies) {
        return Arrays.stream(verifiedCookies)
                .filter(c -> c.getName().equals("Refresh"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("RefreshToken not found!"));
    }

    private Cookie[] verifyCookiesAreNull(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Optional.ofNullable(cookies)
                .orElseThrow(() -> new RuntimeException("Cookie not found!"));
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
