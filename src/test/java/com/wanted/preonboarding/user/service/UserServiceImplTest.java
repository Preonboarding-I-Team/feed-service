package com.wanted.preonboarding.user.service;

import com.wanted.preonboarding.security.config.PasswordEncoderConfig;
import com.wanted.preonboarding.user.dto.UserPost;
import com.wanted.preonboarding.user.dto.UserResponse;
import com.wanted.preonboarding.user.entity.User;
import com.wanted.preonboarding.user.mapper.UserMapper;
import com.wanted.preonboarding.user.repository.UserRepository;
import com.wanted.preonboarding.user.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@Import({UserServiceImpl.class, UserMapper.class, PasswordEncoderConfig.class})
class UserServiceImplTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    UserService userService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    UserRepository userRepository;

    private Long userId;
    private String account;
    private String email;
    private String password;
    private UserPost post;
    private User user;

    @BeforeEach
    void setUp() {
        userId = 1L;
        account = "account";
        email = "test@email.com";
        password = "c2f9x9@43a";

        post = UserPost.builder()
                .account(account)
                .email(email)
                .password(password)
                .build();

        user = User.builder()
                .id(userId)
                .account(account)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
    }

    @DisplayName("save_ok(): 회원 가입 성공")
    @Test
    void save_ok() {
        // given
        given(userRepository.findByAccountOrEmail(account, email)).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(user);
        // when
        UserResponse response = userService.save(post);
        // then
        Assertions.assertThat(response.getUserId()).isEqualTo(userId);
        Assertions.assertThat(response.getAccount()).isEqualTo(account);
        Assertions.assertThat(response.getEmail()).isEqualTo(email);
    }

    @DisplayName("save_error(): 회원 가입 실패")
    @Test
    void save_error() {
        // given
        given(userRepository.findByAccountOrEmail(account, email)).willReturn(Optional.ofNullable(user));
        // when
        Assertions.assertThatThrownBy(() -> userService.save(post))
        // then
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용할 수 없는 account 또는 email 입니다.");
    }
}