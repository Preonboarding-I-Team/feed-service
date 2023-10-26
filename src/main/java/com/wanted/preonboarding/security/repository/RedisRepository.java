package com.wanted.preonboarding.security.repository;

import com.wanted.preonboarding.security.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final StringRedisTemplate redisTemplate;

    private final JwtProperties jwtProperties;

    public void saveRefreshToken(String refreshToken, String account) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(refreshToken, account, Duration.ofMinutes(jwtProperties.getRefreshTokenExpirationMinutes()));
    }

    public String getSubject(String refreshToken) {
        return redisTemplate.opsForValue().get(refreshToken);
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }
}
