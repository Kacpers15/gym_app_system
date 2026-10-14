package com.example.java_lab1.controller;

import com.example.java_lab1.dto.AvailabilityResponse;
import com.example.java_lab1.dto.TrainerResponse;
import com.example.java_lab1.service.TrainerAvailabilityService;
import com.example.java_lab1.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainerAvailabilityService availabilityService;

    @GetMapping
    public List<TrainerResponse> list() {
        return trainerService.findAll();
    }

    @GetMapping("/{id}")
    public TrainerResponse get(@PathVariable Long id) {
        return trainerService.findById(id);
    }

    @GetMapping("/{id}/availability")
    public List<AvailabilityResponse> availability(@PathVariable Long id) {
        return availabilityService.getFreeByTrainer(id);
    }
}