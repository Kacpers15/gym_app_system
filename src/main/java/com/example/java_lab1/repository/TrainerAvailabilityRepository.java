package com.example.java_lab1.repository;

import com.example.java_lab1.model.TrainerAvailability;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainerAvailabilityRepository extends JpaRepository<TrainerAvailability, Long> {

    List<TrainerAvailability> findByTrainerId(Long trainerId);

    List<TrainerAvailability> findByTrainerIdAndBookedFalse(Long trainerId);

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
        FROM TrainerAvailability a
        WHERE a.trainer.id = :trainerId
          AND a.startTime < :endTime
          AND a.endTime > :startTime
          AND (:excludeId IS NULL OR a.id <> :excludeId)
        """)
    boolean existsOverlapping(@Param("trainerId") Long trainerId,
                              @Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime,
                              @Param("excludeId") Long excludeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM TrainerAvailability a WHERE a.id = :id")
    Optional<TrainerAvailability> findByIdForUpdate(@Param("id") Long id);
}