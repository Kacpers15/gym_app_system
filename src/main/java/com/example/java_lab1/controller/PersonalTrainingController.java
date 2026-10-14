package com.example.java_lab1.controller;

import com.example.java_lab1.dto.PersonalTrainingRequest;
import com.example.java_lab1.dto.PersonalTrainingResponse;
import com.example.java_lab1.security.AuthUtils;
import com.example.java_lab1.service.PersonalTrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal-trainings")
@RequiredArgsConstructor
public class PersonalTrainingController {

    private final PersonalTrainingService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonalTrainingResponse book(@Valid @RequestBody PersonalTrainingRequest req,
                                         Authentication auth) {
        return service.book(AuthUtils.currentUserId(auth), req);
    }

    @GetMapping("/me")
    public List<PersonalTrainingResponse> my(Authentication auth) {
        return service.myTrainings(AuthUtils.currentUserId(auth));
    }

    @PatchMapping("/{id}/cancel")
    public PersonalTrainingResponse cancel(@PathVariable Long id, Authentication auth) {
        return service.cancelByUser(AuthUtils.currentUserId(auth), id);
    }
}