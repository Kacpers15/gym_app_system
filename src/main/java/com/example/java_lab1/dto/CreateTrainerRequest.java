package com.example.java_lab1.dto;

import jakarta.validation.constraints.*;

public record CreateTrainerRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password
) {}