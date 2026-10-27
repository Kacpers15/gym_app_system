package com.example.java_lab1.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MembershipTypeRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        @Min(1) int durationDays,
        boolean active
) {}