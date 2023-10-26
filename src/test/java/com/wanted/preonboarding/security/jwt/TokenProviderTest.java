package com.wanted.preonboarding.security.jwt;

import com.wanted.preonboarding.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(TokenProvider.class)
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = JwtProperties.class)
class TokenProviderTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JwtProperties jwtProperties;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TokenProvider tokenProvider;

    private User user;

    private SecretKey secretKey;

    private Map<String, Object> claims;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id(1L)
                .email("test@email.com")
                .build();
        claims = Map.of("id", 1L, "roles", List.of("USER"));
        secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @DisplayName("generateAccessToken(): jwtProperties의 값으로 accessToken 만들기 성공")
    @Test
    void generateAccessToken() {
        // given
        String accessToken = tokenProvider.generateAccessToken(user.getAccount(), user.getId(), user.getEmail());
        Date preExpiration = getPreExpiration(jwtProperties.getAccessTokenExpirationMinutes());
        Date postExpiration = getPostExpiration(jwtProperties.getAccessTokenExpirationMinutes());
        // when
        Claims payload = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
        // then
        Assertions.assertAll(
                () -> assertThat(getSubject(payload)).isEqualTo(user.getAccount()),
                () -> assertThat(getUserId(payload)).isEqualTo(user.getId()),
                () -> assertThat(getEmail(payload)).isEqualTo(user.getEmail()),
                () -> assertThat(getExpiration(payload)).isBetween(preExpiration, postExpiration)
        );
    }

    @DisplayName("generateRefreshToken(): jwtProperties의 값으로 refreshToken 만들기 성공")
    @Test
    void generateRefreshToken() {
        // given
        String refreshToken = tokenProvider.generateRefreshToken(user.getAccount());
        Date preExpiration = getPreExpiration(jwtProperties.getRefreshTokenExpirationMinutes());
        Date postExpiration = getPostExpiration(jwtProperties.getRefreshTokenExpirationMinutes());
        // when
        Claims payload = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();
        // then
        Assertions.assertAll(
                () -> assertThat(getSubject(payload)).isEqualTo(user.getAccount()),
                () -> assertThat(getExpiration(payload)).isBetween(preExpiration, postExpiration)
        );
    }

    @DisplayName("getClaims(): accessToken에서 Claims 가져오기 성공")
    @Test
    void getClaims() {
        // given
        String accessToken = tokenProvider.generateAccessToken(user.getAccount(), user.getId(), user.getEmail());
        Date preExpiration = getPreExpiration(jwtProperties.getAccessTokenExpirationMinutes());
        Date postExpiration = getPostExpiration(jwtProperties.getAccessTokenExpirationMinutes());
        // when
        Claims payload = tokenProvider.getClaims(accessToken);
        // then
        Assertions.assertAll(
                () -> assertThat(getSubject(payload)).isEqualTo(user.getAccount()),
                () -> assertThat(getUserId(payload)).isEqualTo(user.getId()),
                () -> assertThat(getEmail(payload)).isEqualTo(user.getEmail()),
                () -> assertThat(getExpiration(payload)).isBetween(preExpiration, postExpiration)
        );
    }

    @DisplayName("getClaims_SignatureException(): 잘못된 토큰일 때 SignatureException 발생")
    @Test
    void getClaims_SignatureException() {
        //given
        Calendar instance = Calendar.getInstance();
        Date issuedAt = instance.getTime();
        instance.add(Calendar.MINUTE, 10);
        Date expiration = instance.getTime();
        SecretKey invalidSecretKey = Keys.hmacShaKeyFor("InvalidSecretKeyForInvalidTokenTest".getBytes());
        String invalidAccessToken = Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject("subject")
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claims(claims)
                .signWith(invalidSecretKey)
                .compact();
        // when
        assertThatThrownBy(() -> tokenProvider.getClaims(invalidAccessToken))
                // then
                .isInstanceOf(SignatureException.class);
    }

    @DisplayName("getClaims_ExpiredJwtException(): 잘못된 토큰일 때 ExpiredJwtException 발생")
    @Test
    void getClaims_ExpiredJwtException() {
        //given
        Date issuedAt = getExpiration(-20);
        Date expiration = getExpiration(-10);
        String expiredAccessToken = Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject("subject")
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claims(claims)
                .signWith(secretKey)
                .compact();
        // when
        assertThatThrownBy(() -> tokenProvider.getClaims(expiredAccessToken))
                // then
                .isInstanceOf(ExpiredJwtException.class);
    }

    private String getSubject(Claims payload) {
        return payload.getSubject();
    }

    private Long getUserId(Claims payload) {
        return payload.get("id", Long.class);
    }

    private String getEmail(Claims payload) {
        return payload.get("email", String.class);
    }

    private Date getExpiration(Claims payload) {
        return payload.getExpiration();
    }

    private Date getPreExpiration(int expirationMinutes) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MINUTE, expirationMinutes - 1);
        return expiration.getTime();
    }

    private Date getPostExpiration(int expirationMinutes) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MINUTE, expirationMinutes + 1);
        return expiration.getTime();
    }

    private Date getExpiration(int expirationMinutes) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MINUTE, expirationMinutes);
        return expiration.getTime();
    }
}