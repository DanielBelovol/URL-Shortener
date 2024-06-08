package com.example.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.role.Permission.*;

@RequiredArgsConstructor
public enum Role {
    USER(Set.of()),
    ADMIN(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"))),
    MODERATOR(Set.of(new SimpleGrantedAuthority("ROLE_MODERATOR")));

    @Getter
    private final Set<SimpleGrantedAuthority> authorities;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return authorities.stream().collect(Collectors.toList());
    }
}
