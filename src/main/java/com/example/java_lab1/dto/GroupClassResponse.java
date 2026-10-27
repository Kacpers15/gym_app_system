package com.example.java_lab1.dto;

import com.example.java_lab1.model.GroupClassStatus;
import java.time.LocalDateTime;

public record GroupClassResponse(
        Long id,
        String name,
        String description,
        Long trainerId,
        String trainerName,
        LocalDateTime startTime,
        int durationMinutes,
        int maxParticipants,
        int freeSpots,
        GroupClassStatus status
) {}