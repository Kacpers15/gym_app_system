package com.example.java_lab1.service;

import com.example.java_lab1.dto.GroupClassRequest;
import com.example.java_lab1.dto.GroupClassResponse;
import com.example.java_lab1.exception.ConflictException;
import com.example.java_lab1.exception.ResourceNotFoundException;
import com.example.java_lab1.model.*;
import com.example.java_lab1.repository.ClassBookingRepository;
import com.example.java_lab1.repository.GroupClassRepository;
import com.example.java_lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupClassService {

    private final GroupClassRepository groupClassRepository;
    private final ClassBookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<GroupClassResponse> listUpcoming(String name, Long trainerId) {
        LocalDateTime now = LocalDateTime.now();
        List<GroupClass> list;
        if (name != null && !name.isBlank()) {
            list = groupClassRepository.findByNameContainingIgnoreCaseAndStartTimeAfterAndStatus(
                    name, now, GroupClassStatus.SCHEDULED);
        } else if (trainerId != null) {
            list = groupClassRepository.findByTrainerIdAndStartTimeAfterAndStatus(
                    trainerId, now, GroupClassStatus.SCHEDULED);
        } else {
            list = groupClassRepository.findByStartTimeAfterAndStatus(now, GroupClassStatus.SCHEDULED);
        }
        return list.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public GroupClassResponse getById(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public GroupClassResponse create(GroupClassRequest req) {
        validate(req);
        User trainer = requireTrainer(req.trainerId());
        GroupClass gc = new GroupClass();
        apply(gc, req, trainer);
        gc.setStatus(GroupClassStatus.SCHEDULED);
        return toResponse(groupClassRepository.save(gc));
    }

    @Transactional
    public GroupClassResponse update(Long id, GroupClassRequest req) {
        validate(req);
        GroupClass gc = find(id);
        if (gc.getStatus() == GroupClassStatus.CANCELLED) {
            throw new ConflictException("Cannot edit cancelled class");
        }
        apply(gc, req, requireTrainer(req.trainerId()));
        return toResponse(gc);
    }

    @Transactional
    public void delete(Long id) {
        GroupClass gc = find(id);
        long active = bookingRepository.countByGroupClassIdAndStatus(id, ClassBookingStatus.ACTIVE);
        if (active > 0) {
            throw new ConflictException("Cannot delete class with active bookings — cancel it instead");
        }
        groupClassRepository.delete(gc);
    }

    @Transactional
    public GroupClassResponse cancel(Long id) {
        GroupClass gc = find(id);
        gc.setStatus(GroupClassStatus.CANCELLED);
        return toResponse(gc);
    }

    private void apply(GroupClass gc, GroupClassRequest req, User trainer) {
        gc.setName(req.name());
        gc.setDescription(req.description());
        gc.setTrainer(trainer);
        gc.setStartTime(req.startTime());
        gc.setDurationMinutes(req.durationMinutes());
        gc.setMaxParticipants(req.maxParticipants());
    }

    private void validate(GroupClassRequest req) {
        if (!req.startTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("startTime must be in the future");
        }
    }

    private User requireTrainer(Long trainerId) {
        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
        if (trainer.getRole() != Role.TRAINER) {
            throw new IllegalArgumentException("User is not a trainer");
        }
        return trainer;
    }

    private GroupClass find(Long id) {
        return groupClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group class not found"));
    }

    GroupClassResponse toResponse(GroupClass gc) {
        long taken = bookingRepository.countByGroupClassIdAndStatus(gc.getId(), ClassBookingStatus.ACTIVE);
        int free = Math.max(0, gc.getMaxParticipants() - (int) taken);
        return new GroupClassResponse(
                gc.getId(),
                gc.getName(),
                gc.getDescription(),
                gc.getTrainer().getId(),
                gc.getTrainer().getFirstName() + " " + gc.getTrainer().getLastName(),
                gc.getStartTime(),
                gc.getDurationMinutes(),
                gc.getMaxParticipants(),
                free,
                gc.getStatus()
        );
    }
}