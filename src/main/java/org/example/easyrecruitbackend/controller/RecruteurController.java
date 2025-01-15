package org.example.easyrecruitbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.easyrecruitbackend.dto.RecruteurDTO;
import org.example.easyrecruitbackend.service.RecruteurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/recruteurs")
public class RecruteurController {

    private final RecruteurService recruteurService;

    @GetMapping
    public ResponseEntity<List<RecruteurDTO>> getAllRecruteurs() {
        List<RecruteurDTO> recruteurs = recruteurService.getAllRecruteurs();
        return ResponseEntity.ok(recruteurs);
    }

    // Créer un recruteur
    @PostMapping
    public ResponseEntity<?> createRecruteur(@RequestBody RecruteurDTO recruteurDTO) {
        Object result = recruteurService.createRecruteur(recruteurDTO);
        if (result instanceof String) {
            // Si le résultat est une chaîne, cela signifie qu'il y a une erreur (par exemple email déjà pris)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        // Si c'est un RecruteurDTO, on renvoie le recruteur créé avec un status 201
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // Récupérer un recruteur par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRecruteurById(@PathVariable Long id) {
        try {
            RecruteurDTO recruteurDTO = recruteurService.getRecruteurById(id);
            return ResponseEntity.ok(recruteurDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Mettre à jour un recruteur
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecruteur(@PathVariable Long id, @RequestBody RecruteurDTO recruteurDTO) {
        try {
            System.out.println("rec" + recruteurDTO);
            RecruteurDTO updatedRecruteurDTO = recruteurService.updateRecruteur(id, recruteurDTO);
            System.out.println(updatedRecruteurDTO);
            return ResponseEntity.ok(updatedRecruteurDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Supprimer un recruteur
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecruteur(@PathVariable Long id) {
        try {
            recruteurService.deleteRecruteur(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
