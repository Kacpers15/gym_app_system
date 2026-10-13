package com.example.java_lab1.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;          // np. "Wyciskanie sztangi"

    private String description;

    private int reps;             // powtórzenia
    private int sets;             // serie
    private int weight;           // ciężar w kg
}