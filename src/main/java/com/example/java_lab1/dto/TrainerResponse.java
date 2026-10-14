package com.example.java_lab1.dto;

public record TrainerResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {}