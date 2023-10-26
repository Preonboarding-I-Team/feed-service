package com.wanted.preonboarding.security.handler;

import com.wanted.preonboarding.security.repository.RedisRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CustomStatusLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RedisRepository redisRepository;

    @Setter
    private HttpStatus httpStatus;

    public CustomStatusLogoutSuccessHandler(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
        this.httpStatus = HttpStatus.OK;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // TODO: accessToken Blacklist 등록 ?
        Cookie[] cookies = request.getCookies();
        Cookie[] verifiedCookies = Optional.ofNullable(cookies).orElse(new Cookie[]{});
        Cookie refreshCookie = Arrays.stream(verifiedCookies)
                .filter(c -> c.getName().equals("Refresh"))
                .findFirst()
                .orElse(new Cookie("Refresh", ""));
        String refreshToken = refreshCookie.getValue();
        redisRepository.deleteRefreshToken(refreshToken);
        refreshCookie.setMaxAge(0);
        response.setStatus(this.httpStatus.value());
        response.getWriter().flush();
    }
}
