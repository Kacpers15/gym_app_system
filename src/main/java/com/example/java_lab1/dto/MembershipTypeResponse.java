package com.example.java_lab1.dto;

import java.math.BigDecimal;

public record MembershipTypeResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int durationDays,
        boolean active
) {}