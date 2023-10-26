package com.wanted.preonboarding.user.mapper;

import com.wanted.preonboarding.user.dto.UserPost;
import com.wanted.preonboarding.user.dto.UserResponse;
import com.wanted.preonboarding.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserPost post) {
        return User.builder()
                .account(post.getAccount())
                .email(post.getEmail())
                .password(post.getPassword())
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .account(user.getAccount())
                .email(user.getEmail())
                .build();
    }
}
