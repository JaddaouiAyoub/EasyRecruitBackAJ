package org.example.easyrecruitbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Entretien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    @OneToOne(mappedBy = "entretien")
    private OffreStage offre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatEntretien etat = EtatEntretien.NON_TRAITE;

}

