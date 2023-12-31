package com.wanted.preonboarding.user.repository;

import com.wanted.preonboarding.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAccountOrEmail(String account, String email);

    Optional<User> findByAccount(String account);
}
