package com.example.java_lab1.repository;

import com.example.java_lab1.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface GroupClassRepository extends JpaRepository<GroupClass, Long> {
    List<GroupClass> findByStartTimeAfter(LocalDateTime time);
}