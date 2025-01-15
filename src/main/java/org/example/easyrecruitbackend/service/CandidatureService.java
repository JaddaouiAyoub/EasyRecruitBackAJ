package org.example.easyrecruitbackend.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.easyrecruitbackend.dto.CandidatureDTO;
import org.example.easyrecruitbackend.dto.CandidatureEntretienDTO;
import org.example.easyrecruitbackend.entity.*;
import org.example.easyrecruitbackend.mapper.CandidatureMapper;
import org.example.easyrecruitbackend.repository.CandidatRepository;
import org.example.easyrecruitbackend.repository.CandidatureRepository;
import org.example.easyrecruitbackend.repository.OffreStageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidatureService {

    private final OffreStageRepository offreStageRepository;
    private final CandidatureRepository candidatureRepository;
    private final CandidatRepository candidatRepository;
    private final CandidatureMapper candidatureMapper;
    private final AIService aiService;

    @PersistenceContext
    private EntityManager entityManager;



    @Autowired
    public CandidatureService(OffreStageRepository offreStageRepository, CandidatureRepository candidatureRepository,
                              CandidatRepository candidatRepository,
                              CandidatureMapper candidatureMapper, AIService aiService) {
        this.offreStageRepository = offreStageRepository;
        this.candidatureRepository = candidatureRepository;
        this.candidatRepository = candidatRepository;
        this.candidatureMapper = candidatureMapper;
        this.aiService = aiService;
    }

    public String postuler(Long offreId, CandidatureDTO candidatureDTO) {
        // Récupérer l'offre de stage à partir de l'ID
        Optional<OffreStage> offreData = offreStageRepository.findById(offreId);
        if (offreData.isEmpty()) {
            return "Offre de stage non trouvée !";
        }

        // Récupérer le candidat à partir de l'ID
        Optional<Candidat> candidatData = candidatRepository.findById(candidatureDTO.getCandidatId());
        if (candidatData.isEmpty()) {
            return "Candidat non trouvé !";
        }

        OffreStage offre = offreData.get();
        Candidat candidat = candidatData.get();

        // Vérifier si le candidat a déjà postulé
        boolean candidatureExists = offre.getCandidatures().stream()
                .anyMatch(c -> c.getCandidat().getId().equals(candidat.getId()));
        if (candidatureExists) {
            return "Le candidat a déjà postulé pour cette offre !";
        }

        // Mapper la DTO en entité Candidature
        Candidature candidature = candidatureMapper.toEntity(candidatureDTO, candidat, offre);

        // Ajouter une date actuelle si la date n'est pas fournie
        if (candidature.getDate() == null) {
            candidature.setDate(new Date());
        }

        // Ajouter la candidature à l'offre
        offre.getCandidatures().add(candidature);
        offreStageRepository.save(offre);

        // Sauvegarder la candidature dans le repository
//        Candidature savedCandidature = candidatureRepository.save(candidature);

        // Retourner un message avec l'ID de la candidature
//        return "Candidature ajoutée avec succès ! ID de la candidature : " + savedCandidature.getId();
        return "Candidature ajoutée avec succès ! ID de la candidature : " + candidature.getId();

    }

    public List<CandidatureDTO> GetByEtat(EtatCandidature etatCandidature) {
        List<Candidature> candidatures = candidatureRepository.findByEtat(etatCandidature);
        return candidatures.stream()
                .map(candidatureMapper::toDto)
                .collect(Collectors.toList());
    }

    //get all candidatures by offer
    public List<CandidatureDTO> getCandidaturesByOffre(Long offreId) {
        List<Candidature> candidatures = candidatureRepository.findByOffreId(offreId);
        return candidatures.stream()
                .map(candidatureMapper::toDto)
                .collect(Collectors.toList());
    }
    //get all candidatures of a condidat
    public List<CandidatureDTO> getCandidaturesByCandidat(Long candidatId) {
        List<Candidature> candidatures =candidatureRepository.findByCandidatId(candidatId);
        return candidatures.stream()
                .map(candidatureMapper::toDto)
                .collect(Collectors.toList());
    }

    // Supprimer une candidature
    public void deleteCandidature(Long candidatureId) {
        candidatureRepository.deleteById(candidatureId);
    }



    public CandidatureDTO updateCandidature(Long candidatureId, CandidatureDTO candidatureDTO) {
        // Vérifier l'existence de la candidature
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new EntityNotFoundException("Candidature non trouvée !"));

        // Mettre à jour uniquement les champs modifiés
        if (candidatureDTO.getLettreMotivation() != null) {
            candidature.setLettreMotivation(candidatureDTO.getLettreMotivation());
        }
        if (candidatureDTO.getCv() != null) {
            candidature.setCv(candidatureDTO.getCv());
        }
        if (candidatureDTO.getScoreInitial() != null) {
            candidature.setScoreInitial(candidatureDTO.getScoreInitial());
        }
        if (candidatureDTO.getScoreFinal() != null) {
            candidature.setScoreFinal(candidatureDTO.getScoreFinal());
        }
        if (candidatureDTO.getDate() != null) { // Mise à jour de la date
            candidature.setDate(candidatureDTO.getDate());
        }

        // Mettre à jour le candidat si nécessaire
        if (candidatureDTO.getCandidatId() != null) {
            Candidat candidat = candidatRepository.findById(candidatureDTO.getCandidatId())
                    .orElseThrow(() -> new EntityNotFoundException("Candidat non trouvé !"));
            candidature.setCandidat(candidat);
        }

        // Mettre à jour l'offre si nécessaire
        if (candidatureDTO.getOffreId() != null) {
            OffreStage offre = offreStageRepository.findById(candidatureDTO.getOffreId())
                    .orElseThrow(() -> new EntityNotFoundException("Offre de stage non trouvée !"));
            candidature.setOffre(offre);
        }

        // Sauvegarder et retourner le DTO mis à jour
        Candidature updatedCandidature = candidatureRepository.save(candidature);
        return candidatureMapper.toDto(updatedCandidature);
    }

    public List<CandidatureDTO> getCandidaturesByRecruteur(Long recruteurId) {
        // Récupérer les offres associées au recruteur
        List<OffreStage> offres = offreStageRepository.findByRecruteurId(recruteurId);

        // Récupérer toutes les candidatures pour ces offres
        List<Candidature> candidatures = offres.stream()
                .flatMap(offre -> candidatureRepository.findByOffreId(offre.getId()).stream())
                .collect(Collectors.toList());

        // Mapper les candidatures en DTOs
        return candidatures.stream()
                .map(candidatureMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean isAuthorizedForInterview(Long candidatureId) {
        Optional<Candidature> candidature = candidatureRepository.findById(candidatureId);

        // Vérifiez ici les conditions d'autorisation
        return candidature.filter(value -> value.getEtat() == EtatCandidature.INVITE_ENTRETIEN).isPresent();

        // Retourne false si la candidature n'existe pas
    }

    public void updateCandidatureStateByEmail(String email, EtatCandidature newEtat) {
        // Étape 1 : Trouver le candidat
        Candidat candidat = candidatRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Candidat non trouvé pour l'email : " + email));

        // Étape 2 : Trouver la candidature associée
        Candidature candidature = candidatureRepository.findByCandidatId(candidat.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Candidature non trouvée pour le candidat avec l'email : " + email));

        // Étape 3 : Mettre à jour l'état
        candidature.setEtat(newEtat);

        // Étape 4 : mise à jour de la candidature
        updateCandidatureStateById(candidature.getId(), newEtat);
    }
    public void changeStat(Long id, String stat){
            Candidature candidature = candidatureRepository.findById(id).get();
            candidature.setEtat(EtatCandidature.valueOf(stat));
            candidatureRepository.save(candidature);
        // Log propre pour le suivi
//        System.out.println("Candidature mise à jour avec succès pour le candidat avec l'email : " + email);
    }
    public void updateCandidatureStateById(Long candidatureId, EtatCandidature newEtat) {
        int updatedCount = candidatureRepository.updateCandidatureStateById(candidatureId, newEtat);
        if (updatedCount == 0) {
            throw new EntityNotFoundException("Aucune candidature trouvée avec l'ID : " + candidatureId);
        }
    }

    /**
     * Update the final score and report for a given candidature by its ID.
     *
     * @param candidatureId The ID of the candidature to update.
     * @param newScoreFinal The new final score to set.
     * @param newRapport    The new report URL to set.
     * @return The updated candidature, or null if not found.
     */
    public CandidatureDTO updateCandidature(Long candidatureId, Float newScoreFinal, String newRapport) {
        Optional<Candidature> optionalCandidature = candidatureRepository.findById(candidatureId);

        if (optionalCandidature.isPresent()) {
            Candidature candidature = optionalCandidature.get();
            candidature.setScoreFinal(newScoreFinal);
            candidature.setRapport(newRapport);
            candidature.setEtat(EtatCandidature.PASSE_ENTRETIEN); // Mettre l'état à "PASSE_ENTRETIEN" après mise à jour du score

            // Sauvegarder les changements dans la base de données
            Candidature updatedCandidature = candidatureRepository.save(candidature);

            // Mapper l'entité Candidature vers le DTO
            return candidatureMapper.toDto(updatedCandidature); // Retourner un DTO au lieu de l'entité
        } else {
            return null; // Candidature non trouvée
        }
    }


    public List<CandidatureEntretienDTO> getCandidaturesWithInterviews(Long candidatId) {
        // Récupérer toutes les candidatures pour un candidat donné
        List<Candidature> candidatures = candidatureRepository.findByCandidatId(candidatId);

        // Filtrer uniquement les candidatures avec EtatCandidature = INVITE_ENTRETIEN
        return candidatures.stream()
                .filter(c -> c.getEtat().equals(EtatCandidature.INVITE_ENTRETIEN))
                .map(c -> {
                    Entretien entretien = c.getOffre().getEntretien();
                    return new CandidatureEntretienDTO(
                            c.getId(),
                            c.getOffre().getTitre(),
                            c.getOffre().getId(),
                            c.getEtat().name(),
                            entretien != null ? entretien.getId() : null,
                            entretien != null ? entretien.getEtat().name() : null
                    );
                })
                .collect(Collectors.toList());
    }

}
