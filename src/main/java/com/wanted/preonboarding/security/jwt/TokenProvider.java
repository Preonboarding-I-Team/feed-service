package com.wanted.preonboarding.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(String subject, Long id, String email) {
        Calendar now = Calendar.getInstance();
        Date issuedAt = now.getTime();
        Date expiration = getExpiration(now, jwtProperties.getAccessTokenExpirationMinutes());
        Map<String, Object> claims = createClaims(id, email);
        SecretKey secretKey = getSecretKey();
        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }

    private Map<String, Object> createClaims(Long id, String email) {
        return Map.of("id", id, "email", email);
    }

    public String generateRefreshToken(String subject) {
        Calendar now = Calendar.getInstance();
        Date issuedAt = now.getTime();
        Date expiration = getExpiration(now, jwtProperties.getRefreshTokenExpirationMinutes());
        SecretKey secretKey = getSecretKey();
        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date getExpiration(Calendar instance, int expirationMinutes) {
        instance.add(Calendar.MINUTE, expirationMinutes);
        return instance.getTime();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
