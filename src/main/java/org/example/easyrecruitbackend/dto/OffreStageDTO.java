package org.example.easyrecruitbackend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OffreStageDTO {
    private Long id ;
    private String location;
    private String salaire;
    private String titre;
    private String description;
    private LocalDate datePublication = LocalDate.now(); // Définit la date d'aujourd'hui
    private RecruteurDTO recruteurDTO;
    private String photo ;
    private String domaine; // Utiliser String si `Domaine` est un enum, le mapper s'occupera de la conversion
    private String keywords ;
    private Long entretienId ;
}