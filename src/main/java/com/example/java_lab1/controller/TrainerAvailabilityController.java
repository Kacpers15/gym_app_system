package com.example.java_lab1.controller;

import com.example.java_lab1.dto.AvailabilityRequest;
import com.example.java_lab1.dto.AvailabilityResponse;
import com.example.java_lab1.security.AuthUtils;
import com.example.java_lab1.service.TrainerAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainer/availability")
@RequiredArgsConstructor
public class TrainerAvailabilityController {

    private final TrainerAvailabilityService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvailabilityResponse create(@Valid @RequestBody AvailabilityRequest req,
                                       Authentication auth) {
        return service.create(AuthUtils.currentUserId(auth), req);
    }

    @PutMapping("/{id}")
    public AvailabilityResponse update(@PathVariable Long id,
                                       @Valid @RequestBody AvailabilityRequest req,
                                       Authentication auth) {
        return service.update(AuthUtils.currentUserId(auth), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication auth) {
        service.delete(AuthUtils.currentUserId(auth), id);
    }
}