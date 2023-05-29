package ru.mikhailov.claimregistrar.user.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    USER(Set.of(Permission.PERMISSION_USER)),
    OPERATOR(Set.of(Permission.PERMISSION_OPERATOR)),
    ADMIN(Set.of(Permission.PERMISSION_ADMIN));

    private final Set<Permission> permissions;

    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> grantedAuthoritySet() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
