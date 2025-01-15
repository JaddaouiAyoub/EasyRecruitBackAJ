package org.example.easyrecruitbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.easyrecruitbackend.dto.CandidatDTO;
import org.example.easyrecruitbackend.dto.RecruteurDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakAuthService {

    private final RestTemplate restTemplate;
    private final RecruteurService recruteurService;
    private final CandidatService candidatService;

    @Value("${keycloak.base-url}")
    private String keycloakBaseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    /**
     * Authentifie un utilisateur via Keycloak et retourne son access_token et refresh_token.
     */
//    public Map<String, String> login(String username, String password) {
//        String url = keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/token";
//
//        log.info("Tentative de connexion pour l'utilisateur: {}", username);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        String body = "grant_type=password&username=" + username
//                + "&password=" + password
//                + "&client_id=" + clientId
//                + "&client_secret=" + clientSecret;
//
//        HttpEntity<String> request = new HttpEntity<>(body, headers);
//
//        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
//
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            log.info("Authentification réussie pour l'utilisateur: {}", username);
//            return Map.of(
//                    "access_token", (String) response.getBody().get("access_token"),
//                    "refresh_token", (String) response.getBody().get("refresh_token")
//            );
//        } else {
//            log.error("Échec de l'authentification pour l'utilisateur: {}. Réponse: {}", username, response.getBody());
//            throw new RuntimeException("Login failed: " + response.getBody());
//        }
//    }

    /**
     * Authentifie un utilisateur via Keycloak, vérifie son existence en tant que candidat ou recruteur,
     * et retourne ses informations ainsi que les tokens.
     */
    public Map<String, Object> login(String username, String password) {
        String url = keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        log.info("Tentative de connexion pour l'utilisateur: {}", username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=password&username=" + username
                + "&password=" + password
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret;

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("Authentification réussie pour l'utilisateur: {}", username);

            // Récupérer les tokens depuis la réponse
            String accessToken = (String) response.getBody().get("access_token");
            String refreshToken = (String) response.getBody().get("refresh_token");

            // Vérifier l'existence dans la base de données
            Optional<CandidatDTO> candidat = candidatService.findByUsernameAndPassword(username,password);
            Optional<RecruteurDTO> recruteur = recruteurService.findByUsernameAndPassword(username,password);

            if (candidat.isPresent()) {
                log.info("L'utilisateur est un candidat: {}", candidat.get());
                return Map.of(
                        "access_token", accessToken,
                        "refresh_token", refreshToken,
                        "user", candidat.get(),
                        "role", "CANDIDAT"
                );
            } else if (recruteur.isPresent()) {
                log.info("L'utilisateur est un recruteur: {}", recruteur.get());
                return Map.of(
                        "access_token", accessToken,
                        "refresh_token", refreshToken,
                        "user", recruteur.get(),
                        "role", "RECRUTEUR"
                );
            } else {
                log.error("L'utilisateur authentifié n'est ni un candidat ni un recruteur.");
                throw new RuntimeException("User not found in database.");
            }
        } else {
            log.error("Échec de l'authentification pour l'utilisateur: {}. Réponse: {}", username, response.getBody());
            throw new RuntimeException("Login failed: " + response.getBody());
        }
    }


    /**
     * Rafraîchit un token utilisateur en utilisant le refresh_token.
     */
    public Map<String, String> refreshAccessToken(String refreshToken) {
        String url = keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        log.info("Rafraîchissement du token pour le refresh_token fourni.");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=refresh_token&refresh_token=" + refreshToken
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret;

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("Token rafraîchi avec succès.");
            return Map.of(
                    "access_token", (String) response.getBody().get("access_token"),
                    "refresh_token", (String) response.getBody().get("refresh_token")
            );
        } else {
            log.error("Échec du rafraîchissement du token. Réponse: {}", response.getBody());
            throw new RuntimeException("Failed to refresh token: " + response.getBody());
        }
    }
}
