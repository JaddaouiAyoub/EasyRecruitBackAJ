package org.example.easyrecruitbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class KeycloakService {

    private final RestTemplate restTemplate;
    private String cachedToken;
    private long tokenExpirationTime;

    @Value("${keycloak.base-url}")
    private String keycloakBaseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.token-expiration-buffer}")
    private long tokenExpirationBuffer;

    @Value("${keycloak_client_uuid}")
    private String client_uuid;

    public KeycloakService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Récupère un token admin depuis Keycloak.
     */
    public String getAdminToken() {
        long currentTime = System.currentTimeMillis();

        if (cachedToken != null && currentTime < tokenExpirationTime) {
            log.info("Using cached admin token");
            return cachedToken;
        }

        String url = keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        log.info("Requesting admin token from Keycloak: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);

        String body = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((p1, p2) -> p1 + "&" + p2)
                .orElse("");

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String token = (String) response.getBody().get("access_token");
                long expiresIn = ((Number) response.getBody().get("expires_in")).longValue() * 1000;

                this.cachedToken = token;
                this.tokenExpirationTime = currentTime + expiresIn - tokenExpirationBuffer;
                log.info("Admin token retrieved successfully. Expiration in {} ms", expiresIn);
                return token;
            } else {
                log.error("Failed to fetch admin token: {}", response.getBody());
                throw new RuntimeException("Failed to fetch admin token: " + response.getBody());
            }
        } catch (Exception e) {
            log.error("Error while fetching admin token: {}", e.getMessage());
            throw new RuntimeException("Error while fetching admin token", e);
        }
    }

    /**
     * Crée un utilisateur dans Keycloak.
     */
    public String createKeycloakUser(String username, String email, String firstName, String lastName, String password) {
        log.info("Creating Keycloak user: {}", username);
        String adminToken = getAdminToken();

        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("emailVerified", false);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("enabled", true);
        user.put("credentials", List.of(Map.of(
                "temporary", false,
                "type", "password",
                "value", password
        )));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    keycloakBaseUrl + "/admin/realms/" + realm + "/users",
                    request,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to create Keycloak user: {}", response.getBody());
                throw new RuntimeException("Failed to create Keycloak user: " + response.getBody());
            }

            String locationHeader = response.getHeaders().getLocation().toString();
            log.info("User created successfully. User ID: {}", locationHeader.substring(locationHeader.lastIndexOf("/") + 1));
            return locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
        } catch (Exception e) {
            log.error("Error while creating user: {}", e.getMessage());
            throw new RuntimeException("Error while creating Keycloak user", e);
        }
    }

    /**
     * Associe un rôle à un utilisateur dans Keycloak.
     */
    public void assignRoleToUser(String userId, String roleName) {
        log.info("Assigning role {} to user {}", roleName, userId);
        String adminToken = getAdminToken();

        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/clients/" +client_uuid +"/roles/"+ roleName;
        log.info("Fetching role from Keycloak: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Map> roleResponse = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            if (!roleResponse.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to fetch role: {}. Response: {}", roleName, roleResponse.getBody());
                throw new RuntimeException("Failed to fetch role: " + roleName);
            }

            Map<String, Object> role = roleResponse.getBody();
            List<Map<String, Object>> roles = List.of(Map.of(
                    "id", role.get("id"),
                    "name", role.get("name")
            ));

            String assignUrl = keycloakBaseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/clients/" + client_uuid;
            log.info("Assigning role to user. Requesting URL: {}", assignUrl);

            HttpEntity<List<Map<String, Object>>> roleRequest = new HttpEntity<>(roles, headers);
            ResponseEntity<Void> assignResponse = restTemplate.postForEntity(assignUrl, roleRequest, Void.class);

            if (!assignResponse.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to assign role: {} to user ID: {}", roleName, userId);
                throw new RuntimeException("Failed to assign role: " + roleName + " to user ID: " + userId);
            }

            log.info("Role {} assigned successfully to user {}", roleName, userId);
        } catch (Exception e) {
            log.error("Error while assigning role: {}", e.getMessage());
            throw new RuntimeException("Error while assigning role", e);
        }
    }
}
