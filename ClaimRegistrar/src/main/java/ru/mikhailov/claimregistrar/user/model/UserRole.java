package ru.mikhailov.claimregistrar.user.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    USER,
    OPERATOR,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
