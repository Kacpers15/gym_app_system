package com.example.java_lab1.service;

import com.example.java_lab1.dto.AvailabilityRequest;
import com.example.java_lab1.dto.AvailabilityResponse;
import com.example.java_lab1.exception.*;
import com.example.java_lab1.model.Role;
import com.example.java_lab1.model.TrainerAvailability;
import com.example.java_lab1.model.User;
import com.example.java_lab1.repository.TrainerAvailabilityRepository;
import com.example.java_lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerAvailabilityService {

    private final TrainerAvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;

    @Value("${app.personal-training.duration-minutes}")
    private int durationMinutes;

    @Transactional(readOnly = true)
    public List<AvailabilityResponse> getByTrainer(Long trainerId) {
        ensureTrainerExists(trainerId);
        return availabilityRepository.findByTrainerId(trainerId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AvailabilityResponse> getFreeByTrainer(Long trainerId) {
        ensureTrainerExists(trainerId);
        return availabilityRepository.findByTrainerIdAndBookedFalse(trainerId).stream()
                .filter(a -> a.getStartTime().isAfter(LocalDateTime.now()))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AvailabilityResponse create(Long trainerUserId, AvailabilityRequest req) {
        validateRange(req);
        User trainer = userRepository.findById(trainerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
        if (trainer.getRole() != Role.TRAINER) {
            throw new ForbiddenOperationException("Only trainers can add availability");
        }
        if (availabilityRepository.existsOverlapping(
                trainerUserId, req.startTime(), req.endTime(), null)) {
            throw new ConflictException("Availability overlaps with existing slot");
        }

        TrainerAvailability a = new TrainerAvailability();
        a.setTrainer(trainer);
        a.setStartTime(req.startTime());
        a.setEndTime(req.endTime());
        a.setBooked(false);
        return toResponse(availabilityRepository.save(a));
    }

    @Transactional
    public AvailabilityResponse update(Long trainerUserId, Long id, AvailabilityRequest req) {
        validateRange(req);
        TrainerAvailability a = getOwned(trainerUserId, id);
        if (a.isBooked()) {
            throw new ConflictException("Cannot edit booked availability");
        }
        if (availabilityRepository.existsOverlapping(
                trainerUserId, req.startTime(), req.endTime(), id)) {
            throw new ConflictException("Availability overlaps with existing slot");
        }
        a.setStartTime(req.startTime());
        a.setEndTime(req.endTime());
        return toResponse(a);
    }

    @Transactional
    public void delete(Long trainerUserId, Long id) {
        TrainerAvailability a = getOwned(trainerUserId, id);
        if (a.isBooked()) {
            throw new ConflictException("Cannot delete booked availability");
        }
        availabilityRepository.delete(a);
    }

    private TrainerAvailability getOwned(Long trainerUserId, Long id) {
        TrainerAvailability a = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found"));
        if (!a.getTrainer().getId().equals(trainerUserId)) {
            throw new ForbiddenOperationException("Not your availability");
        }
        return a;
    }

    private void validateRange(AvailabilityRequest req) {
        if (!req.endTime().isAfter(req.startTime())) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
        if (req.startTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create availability in the past");
        }
        long minutes = java.time.Duration.between(req.startTime(), req.endTime()).toMinutes();
        if (minutes != durationMinutes) {
            throw new IllegalArgumentException(
                    "Availability duration must be " + durationMinutes + " minutes");
        }
    }

    private void ensureTrainerExists(Long trainerId) {
        User u = userRepository.findById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
        if (u.getRole() != Role.TRAINER) {
            throw new ResourceNotFoundException("User is not a trainer");
        }
    }

    private AvailabilityResponse toResponse(TrainerAvailability a) {
        return new AvailabilityResponse(
                a.getId(), a.getTrainer().getId(),
                a.getStartTime(), a.getEndTime(), a.isBooked());
    }
}