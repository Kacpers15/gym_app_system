package com.example.java_lab1.dto;

import com.example.java_lab1.model.Role;

public record AuthResponse(String token, String email, Role role) {}