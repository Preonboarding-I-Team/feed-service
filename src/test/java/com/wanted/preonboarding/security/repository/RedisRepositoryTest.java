package com.wanted.preonboarding.security.repository;

import com.wanted.preonboarding.security.jwt.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
@DataRedisTest
@Import({
        RedisRepository.class, JwtProperties.class
})
class RedisRepositoryTest {

    @Autowired
    RedisRepository redisRepository;

    private String refreshToken;

    private String account;

    @BeforeEach
    void setUp() {
        refreshToken = "RefreshToken";
        account = "account";
        redisRepository.deleteRefreshToken(refreshToken);
    }

    @DisplayName("saveRefreshToken_getSubject(): 테스트에 성공하면 레디스에 정상적으로 값을 입력하고 찾을 수 있다.")
    @Test
    void saveRefreshToken_getSubject() {
        // given
        redisRepository.saveRefreshToken(refreshToken, account);
        // when
        String result = redisRepository.getSubject(refreshToken);
        // then
        assertThat(result).isEqualTo(account);
    }

    @DisplayName("deleteRefreshToken(): 테스트에 성공하면 레디스에 정상적으로 입력된 값을 삭제할 수 있다.")
    @Test
    void deleteRefreshToken() {
        // given
        redisRepository.saveRefreshToken(refreshToken, account);
        redisRepository.deleteRefreshToken(refreshToken);
        // when
        String result = redisRepository.getSubject(refreshToken);
        // then
        assertThat(result).isNull();
    }

    @DisplayName("deleteNonExistentValues(): 존재하지 않는 값을 삭제해도 에러가 발생하지 않음")
    @Test
    void deleteNonExistentValues() {
        // given
        String nonExistentValue = String.valueOf(Math.random());
        // then
        assertDoesNotThrow(() -> redisRepository.deleteRefreshToken(nonExistentValue));
    }
}