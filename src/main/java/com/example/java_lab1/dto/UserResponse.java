package com.example.java_lab1.dto;

import com.example.java_lab1.model.Role;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        boolean enabled,
        LocalDateTime createdAt
) {}