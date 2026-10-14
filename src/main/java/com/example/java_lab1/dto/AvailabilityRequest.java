package com.example.java_lab1.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AvailabilityRequest(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {}