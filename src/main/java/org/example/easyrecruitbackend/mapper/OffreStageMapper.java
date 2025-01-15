package org.example.easyrecruitbackend.mapper;

import org.example.easyrecruitbackend.dto.OffreStageDTO;
import org.example.easyrecruitbackend.dto.RecruteurDTO;
import org.example.easyrecruitbackend.entity.Domaine;
import org.example.easyrecruitbackend.entity.Entretien;
import org.example.easyrecruitbackend.entity.OffreStage;
import org.example.easyrecruitbackend.entity.Recruteur;

public class OffreStageMapper {

    // Convertir OffreStage -> OffreStageDTO
    public static OffreStageDTO toDTO(OffreStage offreStage) {
        OffreStageDTO dto = new OffreStageDTO();
        dto.setId(offreStage.getId());
        dto.setTitre(offreStage.getTitre());
        dto.setDescription(offreStage.getDescription());
        dto.setDatePublication(offreStage.getDatePublication());
        dto.setDomaine(offreStage.getDomaine().toString());
        dto.setPhoto(offreStage.getPhoto());
        // Convert Recruteur entity to RecruteurDTO
        dto.setRecruteurDTO(RecruteurDTO.toDTO(offreStage.getRecruteur()));
        dto.setKeywords(offreStage.getKeywords());
        dto.setSalaire(offreStage.getSalaire());
        dto.setLocation(offreStage.getLocation());

        // Vérifier si l'entretien existe avant d'accéder à son ID
        if (offreStage.getEntretien() != null) {
            dto.setEntretienId(offreStage.getEntretien().getId());
        }

        return dto;
    }



    // Convertir OffreStageDTO -> OffreStage
    public static OffreStage toEntity(OffreStageDTO dto, Recruteur recruteur, Entretien entretien ) {
        OffreStage offreStage = new OffreStage();
        offreStage.setId(dto.getId());
        offreStage.setTitre(dto.getTitre());
        offreStage.setDescription(dto.getDescription());
        offreStage.setDatePublication(dto.getDatePublication());
        offreStage.setDomaine(Domaine.valueOf(dto.getDomaine())); // Conversion String -> Enum
        offreStage.setRecruteur(recruteur);
        offreStage.setPhoto(dto.getPhoto());
        offreStage.setSalaire(dto.getSalaire());
        offreStage.setLocation(dto.getLocation());
        offreStage.setKeywords(dto.getKeywords());
        offreStage.setEntretien(entretien);
        return offreStage;
    }
}
