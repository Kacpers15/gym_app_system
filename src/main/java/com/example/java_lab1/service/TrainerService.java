package com.example.java_lab1.service;

import com.example.java_lab1.dto.TrainerResponse;
import com.example.java_lab1.exception.ResourceNotFoundException;
import com.example.java_lab1.model.Role;
import com.example.java_lab1.model.User;
import com.example.java_lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TrainerResponse> findAll() {
        return userRepository.findByRole(Role.TRAINER).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TrainerResponse findById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
        if (u.getRole() != Role.TRAINER) {
            throw new ResourceNotFoundException("User is not a trainer");
        }
        return toResponse(u);
    }

    private TrainerResponse toResponse(User u) {
        return new TrainerResponse(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail());
    }
}