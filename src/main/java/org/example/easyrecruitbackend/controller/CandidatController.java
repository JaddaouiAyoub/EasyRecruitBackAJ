package org.example.easyrecruitbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.easyrecruitbackend.dto.CandidatDTO;
import org.example.easyrecruitbackend.service.CandidatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@CrossOrigin("*")
@RequestMapping("/api/candidats")
public class CandidatController {


    private final CandidatService candidatService;

    @GetMapping
    public ResponseEntity<List<CandidatDTO>> getAllCandidats() {
        List<CandidatDTO> candidats = candidatService.getAllCandidats();
        return ResponseEntity.ok(candidats);
    }

    // Créer un candidat
    @PostMapping
    public ResponseEntity<?> createCandidat(@RequestBody CandidatDTO candidatDTO) {
        Object result = candidatService.createCandidat(candidatDTO);
        if (result instanceof String) {
            // Si le résultat est une chaîne, cela signifie qu'il y a une erreur (par exemple email déjà pris)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        // Si c'est un CandidatDTO, on renvoie le candidat créé avec un status 201
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // Récupérer un candidat par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCandidatById(@PathVariable Long id) {
        try {
            CandidatDTO candidatDTO = candidatService.getCandidatById(id);
            return ResponseEntity.ok(candidatDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Mettre à jour un candidat
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCandidat(@PathVariable Long id, @RequestBody CandidatDTO candidatDTO) {
        try {
            CandidatDTO updatedCandidatDTO = candidatService.updateCandidat(id, candidatDTO);
            return ResponseEntity.ok(updatedCandidatDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Supprimer un candidat
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCandidat(@PathVariable Long id) {
        try {
            candidatService.deleteCandidat(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
