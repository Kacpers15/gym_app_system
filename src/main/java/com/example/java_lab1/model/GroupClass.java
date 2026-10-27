package com.example.java_lab1.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GroupClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User trainer;

    private LocalDateTime startTime;
    private int durationMinutes;
    private int maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupClassStatus status = GroupClassStatus.SCHEDULED;

    @Version
    private Long version;
}