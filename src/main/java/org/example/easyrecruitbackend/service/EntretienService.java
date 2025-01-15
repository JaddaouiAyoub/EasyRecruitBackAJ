package org.example.easyrecruitbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.example.easyrecruitbackend.dto.EntretienDTO;
import org.example.easyrecruitbackend.entity.Entretien;
import org.example.easyrecruitbackend.entity.OffreStage;
import org.example.easyrecruitbackend.entity.Question;
import org.example.easyrecruitbackend.repository.OffreStageRepository;
import org.example.easyrecruitbackend.repository.entrtien.EntretienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.easyrecruitbackend.mapper.EntretienMapper.toDTO;

@Service
@Slf4j
public class EntretienService {

    @Autowired
    private EntretienRepository entretienRepository;

    @Autowired
    private  OffreStageRepository offreStageRepository;

    // Créer un entretien avec des questions

    public  EntretienDTO createEntretienForOffre(Long offreId, List<String> questions) {
        // Verify if the offer exists
        OffreStage offre = offreStageRepository.findById(offreId)
                .orElseThrow(() -> new IllegalArgumentException("Offre non trouvée avec l'ID : " + offreId));

        // Check if the offer already has an entretien
        if (offre.getEntretien() != null) {
            throw new IllegalStateException("Cette offre a déjà un entretien associé.");
        }

        // Create a new Entretien
        Entretien entretien = new Entretien();

        // Create and associate questions to the Entretien
        List<Question> questionEntities = questions.stream().map(contenu -> {
            Question question = new Question();
            question.setContenu(contenu);
            question.setEntretien(entretien);
            return question;
        }).collect(Collectors.toList());
        entretien.setQuestions(questionEntities);

        // Associate the Entretien with the Offre
        entretien.setOffre(offre);
        offre.setEntretien(entretien);

        // Save the Entretien
        Entretien savedEntretien = entretienRepository.save(entretien);

        // Return the DTO
        return toDTO(savedEntretien);
    }

    public boolean interviewExists(Long offreId) {
        Optional<OffreStage> optionalOffre = offreStageRepository.findById(offreId);
        return optionalOffre.map(offre -> offre.getEntretien() != null).orElse(false);
    }

    public EntretienDTO getEntretienById(Long entretienId) {
        // Rechercher l'entretien par son ID
        Entretien entretien = entretienRepository.findById(entretienId)
                .orElseThrow(() -> new IllegalArgumentException("Entretien non trouvé avec l'ID : " + entretienId));

        // Convertir l'entité en DTO et la retourner
        return toDTO(entretien);
    }

    public EntretienDTO getEntretienByOffreId(Long offreId) {
        Entretien entretien = offreStageRepository.findEntretienByOffreId(offreId);

        if (entretien == null) {
            throw new IllegalArgumentException("Aucun entretien trouvé pour l'offre avec ID : " + offreId);
        }
        log.info("Entretien trouvé pour l'offre avec ID : " + entretien.getId());
        return toDTO(entretien); // Convertit l'objet Entretien en DTO
    }

}
