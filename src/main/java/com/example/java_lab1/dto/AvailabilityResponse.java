package com.example.java_lab1.dto;

import java.time.LocalDateTime;

public record AvailabilityResponse(
        Long id,
        Long trainerId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean booked
) {}