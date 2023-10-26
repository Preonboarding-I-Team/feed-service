package com.wanted.preonboarding.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.security.annotation.WithSecurityConfig;
import com.wanted.preonboarding.security.dto.UsernamePassword;
import com.wanted.preonboarding.security.jwt.TokenProvider;
import com.wanted.preonboarding.security.repository.RedisRepository;
import com.wanted.preonboarding.security.service.AuthService;
import com.wanted.preonboarding.user.entity.User;
import com.wanted.preonboarding.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@WithSecurityConfig
@Import(AuthService.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RedisRepository redisRepository;

    private User user;

    private Long id;

    private String account;

    private String email;

    private String password;

    private String accessToken;

    private String refreshToken;

    private Cookie cookie;

    @BeforeEach
    void setUp() {
        id = 1L;
        account = "account";
        email = "test@email.com";
        password = "c2f9x9@43a";
        accessToken = tokenProvider.generateAccessToken(account, id, email);
        refreshToken = tokenProvider.generateRefreshToken(account);
        cookie = new Cookie("Refresh", refreshToken);
        user = User.builder()
                .id(id)
                .account(account)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
    }

    @DisplayName("jwtAuthenticationFilter(): 로그인 성공")
    @Test
    void jwtAuthenticationFilter() throws Exception {
        // given
        UsernamePassword usernamePasswordDto = new UsernamePassword(account, password);
        String content = objectMapper.writeValueAsString(usernamePasswordDto);
        given(userRepository.findByAccount(account)).willReturn(Optional.ofNullable(user));
        // when
        ResultActions actions = mockMvc.perform(post("/sign-in")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(cookie().exists("Refresh"));
        verify(userRepository, times(1)).findByAccount(account);
    }

    @DisplayName("jwtAuthenticationFilter_wrongAccount(): 로그인 실패")
    @Test
    void jwtAuthenticationFilter_wrongAccount() throws Exception {
        // given
        UsernamePassword usernamePasswordDto = new UsernamePassword("wrongValue", password);
        String content = objectMapper.writeValueAsString(usernamePasswordDto);
        given(userRepository.findByAccount(account)).willReturn(Optional.ofNullable(user));
        // when
        ResultActions actions = mockMvc.perform(post("/sign-in")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions.andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("AX01"))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Information not matched!"));
    }

    @DisplayName("jwtAuthenticationFilter_wrongPassword(): 로그인 실패")
    @Test
    void jwtAuthenticationFilter_wrongPassword() throws Exception {
        // given
        UsernamePassword usernamePasswordDto = new UsernamePassword(account, "wrongValue");
        String content = objectMapper.writeValueAsString(usernamePasswordDto);
        given(userRepository.findByAccount(account)).willReturn(Optional.ofNullable(user));
        // when
        ResultActions actions = mockMvc.perform(post("/sign-in")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions.andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("AX01"))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Information not matched!"));
    }

    // TODO: JwtVerificationFilter, EntryPoint -> 로그인이 필요한 요청이 만들어지면 테스트

    @DisplayName("logout(): 로그 아웃 성공")
    @Test
    void logout() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(post("/logout")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(cookie));
        // then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().doesNotExist("Refresh"));

    }

    @DisplayName("reissue(): 토큰 재발급 성공")
    @Test
    void reissue() throws Exception {
        // given
        given(userRepository.findByAccount(account)).willReturn(Optional.ofNullable(user));
        given(redisRepository.getSubject(refreshToken)).willReturn(account);
        // when
        ResultActions actions = mockMvc.perform(post("/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(cookie));
        // then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(cookie().exists("Refresh"));
    }
}