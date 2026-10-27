package com.example.java_lab1.service;

import com.example.java_lab1.dto.*;
import com.example.java_lab1.exception.ConflictException;
import com.example.java_lab1.exception.ResourceNotFoundException;
import com.example.java_lab1.model.Role;
import com.example.java_lab1.model.User;
import com.example.java_lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getMe(Long userId) {
        return toResponse(find(userId));
    }

    @Transactional
    public UserResponse updateMe(Long userId, UpdateProfileRequest req) {
        User user = find(userId);
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public UserResponse createTrainer(CreateTrainerRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new ConflictException("Email already registered");
        }
        User trainer = new User();
        trainer.setFirstName(req.firstName());
        trainer.setLastName(req.lastName());
        trainer.setEmail(req.email().trim().toLowerCase());
        trainer.setPassword(passwordEncoder.encode(req.password()));
        trainer.setRole(Role.TRAINER);
        trainer.setEnabled(true);
        return toResponse(userRepository.save(trainer));
    }

    @Transactional
    public UserResponse setEnabled(Long id, boolean enabled) {
        User user = find(id);
        if (user.getRole() == Role.ADMIN) {
            throw new ConflictException("Cannot disable admin");
        }
        user.setEnabled(enabled);
        return toResponse(user);
    }

    private User find(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(), u.getFirstName(), u.getLastName(),
                u.getEmail(), u.getRole(), u.isEnabled(), u.getCreatedAt());
    }
}