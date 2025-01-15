package org.example.easyrecruitbackend.mapper;

import org.example.easyrecruitbackend.dto.CandidatureDTO;
import org.example.easyrecruitbackend.entity.Candidature;
import org.example.easyrecruitbackend.entity.Candidat;
import org.example.easyrecruitbackend.entity.OffreStage;
import org.springframework.stereotype.Component;

@Component
public class CandidatureMapper {

    // Convertit une entité en DTO
    public CandidatureDTO toDto(Candidature candidature) {
        CandidatureDTO dto = new CandidatureDTO();

        dto.setCandidatureId(candidature.getId());
        dto.setCandidatId(candidature.getCandidat().getId());
        dto.setOffreId(candidature.getOffre().getId());
        dto.setLettreMotivation(candidature.getLettreMotivation());
        dto.setCv(candidature.getCv());
        dto.setScoreInitial(candidature.getScoreInitial()); // Ajout du score initial
        dto.setScoreFinal(candidature.getScoreFinal());     // Ajout du score final
        dto.setDate(candidature.getDate()); // Ajout de la date
        dto.setEtat(candidature.getEtat());
        dto.setRapport(candidature.getRapport());
        return dto;
    }

    // Convertit un DTO en entité
    public Candidature toEntity(CandidatureDTO dto, Candidat candidat, OffreStage offre) {
        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setOffre(offre);
        candidature.setLettreMotivation(dto.getLettreMotivation());
        candidature.setCv(dto.getCv());
        candidature.setScoreInitial(dto.getScoreInitial()); // Initialisation du score initial à null
        candidature.setScoreFinal(null);   // Initialisation du score final à null
        candidature.setDate(dto.getDate()); // Ajout de la date
        candidature.setEtat(dto.getEtat());
        candidature.setRapport(dto.getRapport());
        return candidature;
    }
}
