package org.example.easyrecruitbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.easyrecruitbackend.dto.RecruteurDTO;
import org.example.easyrecruitbackend.entity.Recruteur;
import org.example.easyrecruitbackend.mapper.RecruteurMapper;
import org.example.easyrecruitbackend.repository.RecruteurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruteurService {

    private final RecruteurRepository recruteurRepository;
    private final RecruteurMapper recruteurMapper;
    private final KeycloakService keycloakService;

    // Récupérer tous les recruteurs
    public List<RecruteurDTO> getAllRecruteurs() {
        return recruteurRepository.findAll().stream()
                .map(recruteurMapper::toRecruteurDTO)
                .collect(Collectors.toList());
    }

    // Récupérer un recruteur par ID
    public RecruteurDTO getRecruteurById(Long id) {
        return recruteurRepository.findById(id)
                .map(recruteurMapper::toRecruteurDTO)
                .orElseThrow(() -> new RuntimeException("Recruteur introuvable avec ID : " + id));
    }

    // Créer un nouveau recruteur
    @Transactional
    public Object createRecruteur(RecruteurDTO recruteurDTO) {
        // Vérifier si l'email existe déjà
        Optional<Recruteur> existingRecruteur = recruteurRepository.findByUsername(recruteurDTO.getUsername());
        if (existingRecruteur.isPresent()) {
            return "ce username est déjà pris.";
        }

        // Étape 3 : Créer le recruteur dans la base de données
        Recruteur recruteur = recruteurMapper.toRecruteurEntity(recruteurDTO);
        Recruteur savedRecruteur = recruteurRepository.save(recruteur);

        try {
            // Étape 1 : Créer l'utilisateur dans Keycloak
            String userId = keycloakService.createKeycloakUser(
                    recruteurDTO.getUsername(),
                    recruteurDTO.getEmail(),
                    recruteurDTO.getFirstName(),
                    recruteurDTO.getLastName(),
                    recruteurDTO.getPassword()
            );

            // Étape 2 : Assigner le rôle "Recruteur" dans Keycloak
            keycloakService.assignRoleToUser(userId, "RECRUTEUR");



            return recruteurMapper.toRecruteurDTO(savedRecruteur);
        } catch (Exception e) {
            // Gestion des erreurs (Keycloak ou autres)
            return "Erreur lors de la création du recruteur : " + e.getMessage();
        }
    }


    // Mettre à jour un recruteur
        public RecruteurDTO updateRecruteur(Long id, RecruteurDTO recruteurDTO) {
            // Rechercher l'entité existante
            Recruteur recruteur = recruteurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Recruteur introuvable avec ID : " + id));

            // Mettre à jour l'entité avec les données du DTO
            recruteurMapper.updateRecruteurFromDTO(recruteurDTO, recruteur);

            // Sauvegarder les modifications
            Recruteur updatedRecruteur = recruteurRepository.save(recruteur);

            // Retourner l'objet mis à jour sous forme de DTO
            return recruteurMapper.toRecruteurDTO(updatedRecruteur);
        }


        // Supprimer un recruteur
    public void deleteRecruteur(Long id) {
        if (!recruteurRepository.existsById(id)) {
            throw new RuntimeException("Recruteur introuvable avec ID : " + id);
        }
        recruteurRepository.deleteById(id);
    }

    public Optional<RecruteurDTO> findByEmail(String email) {
        return recruteurRepository.findByEmail(email)
                .map(recruteurMapper::toRecruteurDTO);
    }

    public Optional<RecruteurDTO> findByUsernameAndPassword(String username, String password) {
        return recruteurRepository.findByUsernameAndPassword(username,password)
                .map(recruteurMapper::toRecruteurDTO);
    }
}
