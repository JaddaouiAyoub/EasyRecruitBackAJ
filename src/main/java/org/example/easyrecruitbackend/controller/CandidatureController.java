package org.example.easyrecruitbackend.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.easyrecruitbackend.dto.CandidatureDTO;

import org.example.easyrecruitbackend.dto.CandidatureEntretienDTO;
import org.example.easyrecruitbackend.entity.Candidature;
import org.example.easyrecruitbackend.entity.EtatCandidature;

import org.example.easyrecruitbackend.service.CandidatureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/candidatures")
@Slf4j
public class CandidatureController {

    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }


    @PostMapping("/{offreId}")
    public ResponseEntity<Map<String, String>> postuler(@PathVariable Long offreId, @RequestBody CandidatureDTO candidatureDTO) {
        try {
            log.info("candidature to be stored" +  candidatureDTO);
            // Appel du service pour traiter la candidature
            String message = candidatureService.postuler(offreId, candidatureDTO);

            // Préparer la réponse
            Map<String, String> response = new HashMap<>();
            response.put("message", message);

            // Vérifier le message pour définir le statut HTTP et la réponse
            if (message.contains("Offre de stage non trouvée !") || message.contains("Candidat non trouvé !")) {
                // Si l'offre ou le candidat n'a pas été trouvé, renvoyer une réponse avec un code 404
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else if (message.contains("Le candidat a déjà postulé pour cette offre !")) {
                // Si la candidature existe déjà, renvoyer une réponse avec un code 400
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                // Si tout s'est bien passé, renvoyer une réponse avec un code 201 (Created)
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }

        } catch (Exception e) {
            // Gestion des erreurs internes génériques
            return new ResponseEntity<>(Map.of("error", "Erreur lors de l'ajout de la candidature : " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/offre/{recruteurId}/{etat}")
    public ResponseEntity<List<CandidatureDTO>> getByOffreAndEtat(@PathVariable Long recruteurId,@PathVariable String etat) {
        List<CandidatureDTO> candidatures = candidatureService.getCandidaturesByRecruteur(recruteurId);
        List<CandidatureDTO> candidatures2 = candidatures.stream().filter(candidatureDTO -> candidatureDTO.getEtat().equals(EtatCandidature.valueOf(etat))).toList() ;
        return ResponseEntity.ok(candidatures2);
    }

    @GetMapping("/offre/{offreId}")
    public ResponseEntity<List<CandidatureDTO>> getByOffre(@PathVariable Long offreId) {
        List<CandidatureDTO> candidatures = candidatureService.getCandidaturesByOffre(offreId);
        return ResponseEntity.ok(candidatures);
    }

    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<List<CandidatureDTO>> getByCandidat(@PathVariable Long candidatId) {
        return ResponseEntity.ok(candidatureService.getCandidaturesByCandidat(candidatId));
    }

    @DeleteMapping("/{candidatureId}")
    public ResponseEntity<Void> delete(@PathVariable Long candidatureId) {
        candidatureService.deleteCandidature(candidatureId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{candidatureId}")
    public ResponseEntity<CandidatureDTO> updateCandidature(
            @PathVariable Long candidatureId, @RequestBody CandidatureDTO candidatureDTO) {
        CandidatureDTO updatedCandidature = candidatureService.updateCandidature(candidatureId, candidatureDTO);
        return ResponseEntity.ok(updatedCandidature);
    }

    @GetMapping("/recruteur/{recruteurId}")
    public ResponseEntity<List<CandidatureDTO>> getCandidaturesByRecruteur(@PathVariable Long recruteurId) {
        List<CandidatureDTO> candidatures = candidatureService.getCandidaturesByRecruteur(recruteurId);
        return ResponseEntity.ok(candidatures);
    }
    @GetMapping("/{id}/isAuthorized")
    public ResponseEntity<Boolean> checkAuthorization(@PathVariable Long id) {
        boolean isAuthorized = candidatureService.isAuthorizedForInterview(id);
        return ResponseEntity.ok(isAuthorized);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<CandidatureDTO> updateCandidature(
            @PathVariable Long id,
            @RequestParam Float scoreFinal,
            @RequestParam String rapport) {
        CandidatureDTO updatedCandidature = candidatureService.updateCandidature(id, scoreFinal, rapport);

        if (updatedCandidature != null) {
            return ResponseEntity.ok(updatedCandidature);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/with-interviews")
    public List<CandidatureEntretienDTO> getCandidaturesWithInterviews(@RequestParam Long candidatId) {
        return candidatureService.getCandidaturesWithInterviews(candidatId);
    }
}
