package com.wanted.preonboarding.security.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = JwtProperties.class)
class JwtPropertiesTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    JwtProperties jwtProperties;

    @DisplayName("readPropertiesValue(): 값을 읽었다면 null이 아니거나, 0이 아니다.")
    @Test
    void readPropertiesValue() {
        assertThat(jwtProperties.getIssuer()).isNotNull();
        assertThat(jwtProperties.getSecret()).isNotNull();
        assertThat(jwtProperties.getAccessTokenExpirationMinutes()).isNotZero();
        assertThat(jwtProperties.getRefreshTokenExpirationMinutes()).isNotZero();
    }
}