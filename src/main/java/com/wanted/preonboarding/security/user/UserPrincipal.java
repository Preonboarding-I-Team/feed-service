package com.wanted.preonboarding.security.user;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserPrincipal extends User {

    private final Long id;

    private final String email;

    public UserPrincipal(com.wanted.preonboarding.user.entity.User user) {
        super(user.getAccount(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.id = user.getId();
        this.email = user.getEmail();
    }

    public UserPrincipal(Claims claims) {
        super(claims.getSubject(), "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.id = claims.get("id", Long.class);
        this.email = claims.get("email", String.class);
    }
}
