package com.example.java_lab1.security;

import org.springframework.security.core.Authentication;

public final class AuthUtils {
    private AuthUtils() {}

    public static Long currentUserId(Authentication auth) {
        return ((CustomUserDetails) auth.getPrincipal()).getId();
    }
}