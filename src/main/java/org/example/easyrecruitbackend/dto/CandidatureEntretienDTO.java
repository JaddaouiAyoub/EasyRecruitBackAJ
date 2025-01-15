package org.example.easyrecruitbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidatureEntretienDTO {
    private Long candidatureId;
    private String offreTitre; // Titre de l'offre
    private Long offreId;
    private String etatCandidature;

    // Informations sur l'entretien associé
    private Long entretienId;
    private String etatEntretien; // État de l'entretien
}
