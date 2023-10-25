package com.wanted.preonboarding.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.security.annotation.CustomWebMvcTest;
import com.wanted.preonboarding.user.dto.UserPost;
import com.wanted.preonboarding.user.dto.UserResponse;
import com.wanted.preonboarding.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@CustomWebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    private UserPost post;

    @BeforeEach
    void setUp() {
        String account = "account";
        String email = "test@email.com";
        String password = "c2f9x9@43a";
        post = UserPost.builder()
                .account(account)
                .email(email)
                .password(password)
                .build();
    }

    @DisplayName("postUser_ok(): 입력값 유효성 검사 성공")
    @Test
    void postUser_ok() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(post);
        given(userService.save(any(UserPost.class))).willReturn(new UserResponse());
        // when
        ResultActions actions = mockMvc.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("postUser_validationPassword(): password 유효성 검사 실패")
    @ParameterizedTest(name = "실패 이유: {1}")
    @CsvSource({
            "12ab#$78c, 10자 이하 문자열은 허용하지 않습니다.",
            "1234567890, 숫자로만 이루어질 수 없습니다.",
            "sdfsdxcvdw, 알파벳으로만 이루어질 수 없습니다.",
            "aaa1z$2v2t, 동일한 문자 3번 연속으로 사용할 수 없습니다.",
            "password2#, 자주 사용되는 문자열 사용할 수 없습니다."
    })
    void postUser_validationPassword(String password, String reason) throws Exception {
        // given
        post.setPassword(password);
        String content = objectMapper.writeValueAsString(post);
        given(userService.save(any(UserPost.class))).willReturn(new UserResponse());
        // when
        ResultActions actions = mockMvc.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("postUser_validationEmail(): email 유효성 검사 실패")
    @ParameterizedTest(name = "email: {0} 유효성 검사")
    @CsvSource({
            ", null은 허용하지 않습니다.",
            "notEmailPattern, Email 형식이 아닙니다."
    })
    void postUser_validationEmail(String email, String reason) throws Exception {
        // given
        post.setEmail(email);
        String content = objectMapper.writeValueAsString(post);
        System.out.println("content = " + content);
        given(userService.save(any(UserPost.class))).willReturn(new UserResponse());
        // when
        ResultActions actions = mockMvc.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("postUser_validationAccount(): account 유효성 검사 실패")
    @ParameterizedTest(name = "account: {1} 유효성 검사")
    @CsvSource(", null은 허용하지 않습니다.")
    void postUser_validationAccount(String account, String reason) throws Exception {
        // given
        post.setAccount(account);
        String content = objectMapper.writeValueAsString(post);
        System.out.println("content = " + content);
        given(userService.save(any(UserPost.class))).willReturn(new UserResponse());
        // when
        ResultActions actions = mockMvc.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions.andDo(print())
                .andExpect(status().isBadRequest());
    }
}