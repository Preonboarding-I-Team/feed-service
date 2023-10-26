package com.wanted.preonboarding.user.service;

import com.wanted.preonboarding.user.dto.UserPost;
import com.wanted.preonboarding.user.dto.UserResponse;

public interface UserService {

    UserResponse save(UserPost post);
}
