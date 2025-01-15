package org.example.easyrecruitbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Candidature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Candidat candidat;
    @ManyToOne
    private OffreStage offre;
    private String lettreMotivation;
    private String cv;
    private String rapport = null;

    //@OneToOne(cascade = CascadeType.ALL)
    //private EntretienVideo entretienVideo;
    @Column(nullable = true)
    private Float scoreInitial;
    private Float scoreFinal= (float) 0;
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatCandidature etat = EtatCandidature.NON_TRAITE; // État par défaut
}
