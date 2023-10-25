package com.wanted.preonboarding.user.service.impl;

import com.wanted.preonboarding.user.dto.UserPost;
import com.wanted.preonboarding.user.dto.UserResponse;
import com.wanted.preonboarding.user.entity.User;
import com.wanted.preonboarding.user.mapper.UserMapper;
import com.wanted.preonboarding.user.repository.UserRepository;
import com.wanted.preonboarding.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse save(UserPost post) {
        validationAccountAndEmail(post);
        post.setPassword(passwordEncoder.encode(post.getPassword()));
        User user = userMapper.toEntity(post);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    private void validationAccountAndEmail(UserPost post) {
        userRepository.findByAccountOrEmail(post.getAccount(), post.getEmail())
                .ifPresent(user -> {
                    throw new RuntimeException("사용할 수 없는 account 또는 email 입니다.");
                });
    }
}
