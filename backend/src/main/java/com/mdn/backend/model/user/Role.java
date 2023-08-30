package com.mdn.backend.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public enum Role {

    USER(
            Set.of(
                    Permission.USER_READ,
                    Permission.USER_CREATE,
                    Permission.USER_UPDATE,
                    Permission.USER_DELETE
            )
    ),
    WAITER(
            Set.of(
                    Permission.WAITER_READ,
                    Permission.WAITER_CREATE,
                    Permission.WAITER_UPDATE,
                    Permission.WAITER_DELETE
            )
    ),
    ADMIN(
            Set.of(
                    Permission.ADMIN_READ,
                    Permission.ADMIN_CREATE,
                    Permission.ADMIN_UPDATE,
                    Permission.ADMIN_DELETE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
