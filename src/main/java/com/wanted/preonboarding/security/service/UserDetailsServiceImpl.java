package com.wanted.preonboarding.security.service;

import com.wanted.preonboarding.security.user.UserPrincipal;
import com.wanted.preonboarding.user.entity.User;
import com.wanted.preonboarding.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccount(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return new UserPrincipal(user);
    }
}
