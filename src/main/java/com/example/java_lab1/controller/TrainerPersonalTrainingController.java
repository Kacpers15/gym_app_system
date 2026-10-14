package com.example.java_lab1.controller;

import com.example.java_lab1.dto.PersonalTrainingResponse;
import com.example.java_lab1.security.AuthUtils;
import com.example.java_lab1.service.PersonalTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainer/personal-trainings")
@RequiredArgsConstructor
public class TrainerPersonalTrainingController {

    private final PersonalTrainingService service;

    @GetMapping
    public List<PersonalTrainingResponse> list(Authentication auth) {
        return service.trainerTrainings(AuthUtils.currentUserId(auth));
    }

    @PatchMapping("/{id}/complete")
    public PersonalTrainingResponse complete(@PathVariable Long id, Authentication auth) {
        return service.completeByTrainer(AuthUtils.currentUserId(auth), id);
    }
}