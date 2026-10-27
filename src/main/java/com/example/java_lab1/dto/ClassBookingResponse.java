package com.example.java_lab1.dto;

import com.example.java_lab1.model.ClassBookingStatus;
import java.time.LocalDateTime;

public record ClassBookingResponse(
        Long id,
        Long userId,
        Long groupClassId,
        String groupClassName,
        LocalDateTime createdAt,
        ClassBookingStatus status
) {}