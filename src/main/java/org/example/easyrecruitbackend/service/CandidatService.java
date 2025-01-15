package org.example.easyrecruitbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.easyrecruitbackend.dto.CandidatDTO;
import org.example.easyrecruitbackend.entity.Candidat;
import org.example.easyrecruitbackend.mapper.CandidatMapper;
import org.example.easyrecruitbackend.repository.CandidatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatService {

    private final CandidatRepository candidatRepository;
    private final CandidatMapper candidatMapper;
    private final KeycloakService keycloakService;

    // Récupérer tous les candidats
    public List<CandidatDTO> getAllCandidats() {
        return candidatRepository.findAll().stream()
                .map(candidatMapper::toCandidatDTO)
                .collect(Collectors.toList());
    }

    // Récupérer un candidat par ID
    public CandidatDTO getCandidatById(Long id) {
        return candidatRepository.findById(id)
                .map(candidatMapper::toCandidatDTO)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable avec ID : " + id));
    }

    // Créer un nouveau candidat
    @Transactional
    public Object createCandidat(CandidatDTO candidatDTO) {
        // Vérifier si l'email existe déjà
        Optional<Candidat> existingCandidat = candidatRepository.findByUsername(candidatDTO.getUsername());
        if (existingCandidat.isPresent()) {
            return "ce username est déjà pris.";
        }

        // Étape 3 : Créer le candidat dans la base de données
        Candidat candidat = candidatMapper.toCandidatEntity(candidatDTO);
        Candidat savedCandidat = candidatRepository.save(candidat);

        try {
            // Étape 1 : Créer l'utilisateur dans Keycloak
            String userId = keycloakService.createKeycloakUser(
                    candidatDTO.getUsername(),
                    candidatDTO.getEmail(),
                    candidatDTO.getFirstName(),
                    candidatDTO.getLastName(),
                    candidatDTO.getPassword()
            );

            // Étape 2 : Assigner le rôle "Candidat" dans Keycloak
            keycloakService.assignRoleToUser(userId, "CANDIDAT");



            return candidatMapper.toCandidatDTO(savedCandidat);
        } catch (Exception e) {
            // Gestion des erreurs (Keycloak ou autres)
            return "Erreur lors de la création du candidat : " + e.getMessage();
        }
    }


    // Mettre à jour un candidat
    public CandidatDTO updateCandidat(Long id, CandidatDTO candidatDTO) {
        Candidat candidat = candidatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable avec ID : " + id));

        // Mettre à jour les champs
        candidatMapper.toCandidatEntity(candidatDTO); // Utilisation de ModelMapper pour mettre à jour l'entité
        Candidat updatedCandidat = candidatRepository.save(candidat);
        return candidatMapper.toCandidatDTO(updatedCandidat);
    }

    // Supprimer un candidat
    public void deleteCandidat(Long id) {
        if (!candidatRepository.existsById(id)) {
            throw new RuntimeException("Candidat introuvable avec ID : " + id);
        }
        candidatRepository.deleteById(id);
    }

    public Optional<CandidatDTO> findByEmail(String email) {
        return candidatRepository.findByEmail(email)
                .map(candidatMapper::toCandidatDTO);
    }
    public Optional<CandidatDTO> findByUsernameAndPassword(String username , String password) {
        return candidatRepository.findByUsernameAndPassword(username,password)
                .map(candidatMapper::toCandidatDTO);
    }
}
