package com.example.java_lab1.repository;

import com.example.java_lab1.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonalTrainingRepository extends JpaRepository<PersonalTraining, Long> {
    List<PersonalTraining> findByUserId(Long userId);
}