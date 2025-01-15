package org.example.easyrecruitbackend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;

    @ManyToOne(fetch = FetchType.LAZY) // Relation avec l'entité Entretien
    @JoinColumn(name = "entretien_id") // Colonne pour stocker la clé étrangère
    private Entretien entretien;
}

