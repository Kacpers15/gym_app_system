package com.example.java_lab1.dto;

import com.example.java_lab1.model.BookingStatus;
import java.time.LocalDateTime;

public record PersonalTrainingResponse(
        Long id,
        Long userId,
        Long trainerId,
        Long availabilityId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BookingStatus status
) {}