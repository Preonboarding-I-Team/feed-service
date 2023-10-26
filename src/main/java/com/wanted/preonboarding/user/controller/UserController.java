package com.wanted.preonboarding.user.controller;

import com.wanted.preonboarding.user.dto.UserPost;
import com.wanted.preonboarding.user.dto.UserResponse;
import com.wanted.preonboarding.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> postUser(@RequestBody @Validated UserPost post) {
        UserResponse response = userService.save(post);
        return ResponseEntity.ok(response);
    }
}
