package org.example.easyrecruitbackend.controller;

import org.example.easyrecruitbackend.dto.UserRequestDTO;
import org.example.easyrecruitbackend.service.KeycloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final KeycloakService keycloakService;

    public UserController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PostMapping
    public ResponseEntity<String> createUserWithRole(@RequestBody UserRequestDTO userRequest) {
        String userId = keycloakService.createKeycloakUser(
                userRequest.getUsername(),
                userRequest.getEmail(),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getPassword()

        );

        keycloakService.assignRoleToUser(userId, userRequest.getRoleName());

        return ResponseEntity.ok("User created successfully and role assigned.");
    }
}

