package com.example.java_lab1.repository;

import com.example.java_lab1.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {
    long countByGroupClassId(Long classId);
    boolean existsByUserIdAndGroupClassId(Long userId, Long classId);
    List<ClassBooking> findByUserId(Long userId);
}