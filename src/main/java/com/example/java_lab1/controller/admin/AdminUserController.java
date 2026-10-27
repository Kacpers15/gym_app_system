package com.example.java_lab1.controller.admin;

import com.example.java_lab1.dto.CreateTrainerRequest;
import com.example.java_lab1.dto.UserResponse;
import com.example.java_lab1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> list() {
        return userService.listAll();
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping("/trainers")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createTrainer(@Valid @RequestBody CreateTrainerRequest req) {
        return userService.createTrainer(req);
    }

    @PatchMapping("/{id}/enabled")
    public UserResponse setEnabled(@PathVariable Long id, @RequestParam boolean enabled) {
        return userService.setEnabled(id, enabled);
    }
}