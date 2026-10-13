package com.example.java_lab1.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrainerAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User trainer;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean booked = false;  // true, gdy ktoś zarezerwował ten slot
}