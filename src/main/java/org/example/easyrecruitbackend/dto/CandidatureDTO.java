package org.example.easyrecruitbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.easyrecruitbackend.entity.EtatCandidature;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidatureDTO {

    private Long candidatureId;
    private Long candidatId;        // ID du candidat
    private Long offreId;           // ID de l'offre
    private String lettreMotivation;
    private String cv;
    private Float scoreInitial;     // Score initial du CV
    private Float scoreFinal;
    private Date date;
    private EtatCandidature etat ;
    private String rapport ;

}
