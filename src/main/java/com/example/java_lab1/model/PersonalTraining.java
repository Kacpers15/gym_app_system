package com.example.java_lab1.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PersonalTraining {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private TrainerAvailability availability;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.BOOKED;
}