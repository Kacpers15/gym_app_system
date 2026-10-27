package com.example.java_lab1.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record GroupClassRequest(
        @NotBlank String name,
        String description,
        @NotNull Long trainerId,
        @NotNull LocalDateTime startTime,
        @Min(15) int durationMinutes,
        @Min(1) int maxParticipants
) {}