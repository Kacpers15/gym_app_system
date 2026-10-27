package com.example.java_lab1.repository;

import com.example.java_lab1.model.ClassBooking;
import com.example.java_lab1.model.ClassBookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {

    long countByGroupClassIdAndStatus(Long classId, ClassBookingStatus status);

    boolean existsByUserIdAndGroupClassIdAndStatus(Long userId, Long classId, ClassBookingStatus status);

    Optional<ClassBooking> findByUserIdAndGroupClassIdAndStatus(
            Long userId, Long classId, ClassBookingStatus status);

    List<ClassBooking> findByUserId(Long userId);

    List<ClassBooking> findByGroupClassId(Long classId);
}