package com.example.java_lab1.service;

import com.example.java_lab1.dto.ClassBookingResponse;
import com.example.java_lab1.exception.*;
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
public class ClassBookingService {

    private final ClassBookingRepository bookingRepository;
    private final GroupClassRepository groupClassRepository;
    private final UserRepository userRepository;

    @Transactional
    public ClassBookingResponse enroll(Long userId, Long classId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        GroupClass gc = groupClassRepository.findByIdForUpdate(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Group class not found"));

        if (gc.getStatus() != GroupClassStatus.SCHEDULED) {
            throw new ConflictException("Cannot enroll in cancelled/completed class");
        }
        if (!gc.getStartTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot enroll in past class");
        }
        if (bookingRepository.existsByUserIdAndGroupClassIdAndStatus(
                userId, classId, ClassBookingStatus.ACTIVE)) {
            throw new ConflictException("Already enrolled");
        }

        long taken = bookingRepository.countByGroupClassIdAndStatus(classId, ClassBookingStatus.ACTIVE);
        if (taken >= gc.getMaxParticipants()) {
            throw new ConflictException("No free spots");
        }

        ClassBooking booking = new ClassBooking();
        booking.setUser(user);
        booking.setGroupClass(gc);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setStatus(ClassBookingStatus.ACTIVE);
        return toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public void cancelMyEnrollment(Long userId, Long classId) {
        ClassBooking booking = bookingRepository
                .findByUserIdAndGroupClassIdAndStatus(userId, classId, ClassBookingStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active enrollment not found"));
        booking.setStatus(ClassBookingStatus.CANCELLED);
    }

    @Transactional(readOnly = true)
    public List<ClassBookingResponse> myBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClassBookingResponse> listForClass(Long classId) {
        if (!groupClassRepository.existsById(classId)) {
            throw new ResourceNotFoundException("Group class not found");
        }
        return bookingRepository.findByGroupClassId(classId).stream()
                .map(this::toResponse)
                .toList();
    }

    private ClassBookingResponse toResponse(ClassBooking b) {
        return new ClassBookingResponse(
                b.getId(),
                b.getUser().getId(),
                b.getGroupClass().getId(),
                b.getGroupClass().getName(),
                b.getCreatedAt(),
                b.getStatus()
        );
    }
}