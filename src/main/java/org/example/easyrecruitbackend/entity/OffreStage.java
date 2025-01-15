package org.example.easyrecruitbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class OffreStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    private LocalDate datePublication;
    private String salaire;
    private String keywords ;
    private String photo ;
    private String location ;

    @Enumerated(EnumType.STRING)
    private Domaine domaine;

    @ManyToOne
    @JoinColumn(name = "recruteur_id", nullable = false)
    private Recruteur recruteur;

    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL)
    private List<Candidature> candidatures;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "entretien_id", referencedColumnName = "id")
    private Entretien entretien;

}
