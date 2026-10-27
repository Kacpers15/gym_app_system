package com.example.java_lab1.repository;

import com.example.java_lab1.model.GroupClass;
import com.example.java_lab1.model.GroupClassStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GroupClassRepository extends JpaRepository<GroupClass, Long> {

    List<GroupClass> findByStartTimeAfterAndStatus(LocalDateTime time, GroupClassStatus status);

    List<GroupClass> findByNameContainingIgnoreCaseAndStartTimeAfterAndStatus(
            String name, LocalDateTime time, GroupClassStatus status);

    List<GroupClass> findByTrainerIdAndStartTimeAfterAndStatus(
            Long trainerId, LocalDateTime time, GroupClassStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM GroupClass g WHERE g.id = :id")
    Optional<GroupClass> findByIdForUpdate(@Param("id") Long id);
}