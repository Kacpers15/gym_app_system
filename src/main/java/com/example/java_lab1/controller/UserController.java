package com.example.java_lab1.controller;

import com.example.java_lab1.dto.UpdateProfileRequest;
import com.example.java_lab1.dto.UserResponse;
import com.example.java_lab1.security.AuthUtils;
import com.example.java_lab1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse me(Authentication auth) {
        return userService.getMe(AuthUtils.currentUserId(auth));
    }

    @PutMapping("/me")
    public UserResponse updateMe(@Valid @RequestBody UpdateProfileRequest req,
                                 Authentication auth) {
        return userService.updateMe(AuthUtils.currentUserId(auth), req);
    }
}