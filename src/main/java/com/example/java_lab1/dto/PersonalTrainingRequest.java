package com.example.java_lab1.dto;

import jakarta.validation.constraints.NotNull;

public record PersonalTrainingRequest(
        @NotNull Long availabilityId
) {}