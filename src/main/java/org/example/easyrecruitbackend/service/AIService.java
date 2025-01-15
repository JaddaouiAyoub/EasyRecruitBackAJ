package org.example.easyrecruitbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    private final RestTemplate restTemplate;

    @Autowired
    public AIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Float evaluateCV(String cvLink) {
        try {
            // Construire la requête pour l'API
            String aiApiUrl = "http://python-ai-service/evaluate-cv";
            Map<String, String> request = new HashMap<>();
            request.put("cvLink", cvLink);

            // Appeler l'API Python
            ResponseEntity<Map> response = restTemplate.postForEntity(aiApiUrl, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object score = response.getBody().get("score");
                return score != null ? Float.parseFloat(score.toString()) : null;
            } else {
                throw new RuntimeException("Échec de l'évaluation du CV : " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'appel à l'API AI : " + e.getMessage());
        }
    }
}
