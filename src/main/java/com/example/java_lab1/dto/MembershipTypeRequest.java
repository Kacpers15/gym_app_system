package com.example.java_lab1.dto;

import java.math.BigDecimal;

public record MembershipTypeRequest(
        String name,
        BigDecimal price,
        int durationDays,
        boolean active
) {}