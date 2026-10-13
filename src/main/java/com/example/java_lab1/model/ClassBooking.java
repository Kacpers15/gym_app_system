package com.example.java_lab1.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "group_class_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClassBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GroupClass groupClass;

    private LocalDateTime createdAt = LocalDateTime.now();
}