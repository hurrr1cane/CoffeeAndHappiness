package com.mdn.backend.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {


    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    WAITER_READ("waiter:read"),
    WAITER_CREATE("waiter:create"),
    WAITER_UPDATE("waiter:update"),
    WAITER_DELETE("waiter:delete"),

    USER_READ("user:read"),
    USER_CREATE("user:create"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete");

    @Getter
    private final String permission;

}
