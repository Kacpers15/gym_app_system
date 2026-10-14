package com.example.java_lab1.service;

import com.example.java_lab1.dto.PersonalTrainingRequest;
import com.example.java_lab1.dto.PersonalTrainingResponse;
import com.example.java_lab1.exception.*;
import com.example.java_lab1.model.*;
import com.example.java_lab1.repository.PersonalTrainingRepository;
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
public class PersonalTrainingService {

    private final PersonalTrainingRepository trainingRepository;
    private final TrainerAvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;

    @Value("${app.cancellation.min-hours-before}")
    private int minHoursBeforeCancel;

    @Transactional
    public PersonalTrainingResponse book(Long userId, PersonalTrainingRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // lock zapobiega podwójnej rezerwacji tego samego slotu
        TrainerAvailability slot = availabilityRepository.findByIdForUpdate(req.availabilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found"));

        if (slot.isBooked()) {
            throw new ConflictException("Slot already booked");
        }
        if (!slot.getStartTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot book past slot");
        }

        slot.setBooked(true);

        PersonalTraining pt = new PersonalTraining();
        pt.setUser(user);
        pt.setAvailability(slot);
        pt.setStatus(BookingStatus.BOOKED);

        return toResponse(trainingRepository.save(pt));
    }

    @Transactional(readOnly = true)
    public List<PersonalTrainingResponse> myTrainings(Long userId) {
        return trainingRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PersonalTrainingResponse> trainerTrainings(Long trainerId) {
        return trainingRepository.findByAvailabilityTrainerId(trainerId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PersonalTrainingResponse cancelByUser(Long userId, Long trainingId) {
        PersonalTraining pt = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new ResourceNotFoundException("Training not found"));
        if (!pt.getUser().getId().equals(userId)) {
            throw new ForbiddenOperationException("Not your training");
        }
        if (pt.getStatus() != BookingStatus.BOOKED) {
            throw new ConflictException("Training is not active");
        }
        LocalDateTime start = pt.getAvailability().getStartTime();
        if (start.minusHours(minHoursBeforeCancel).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Too late to cancel");
        }
        pt.setStatus(BookingStatus.CANCELLED);
        pt.getAvailability().setBooked(false);
        return toResponse(pt);
    }

    @Transactional
    public PersonalTrainingResponse completeByTrainer(Long trainerId, Long trainingId) {
        PersonalTraining pt = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new ResourceNotFoundException("Training not found"));
        if (!pt.getAvailability().getTrainer().getId().equals(trainerId)) {
            throw new ForbiddenOperationException("Not your training");
        }
        if (pt.getStatus() != BookingStatus.BOOKED) {
            throw new ConflictException("Training is not active");
        }
        pt.setStatus(BookingStatus.COMPLETED);
        return toResponse(pt);
    }

    private PersonalTrainingResponse toResponse(PersonalTraining pt) {
        TrainerAvailability a = pt.getAvailability();
        return new PersonalTrainingResponse(
                pt.getId(),
                pt.getUser().getId(),
                a.getTrainer().getId(),
                a.getId(),
                a.getStartTime(),
                a.getEndTime(),
                pt.getStatus()
        );
    }
}