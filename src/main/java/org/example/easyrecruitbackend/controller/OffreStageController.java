package org.example.easyrecruitbackend.controller;

import org.example.easyrecruitbackend.dto.CandidatureDTO;
import org.example.easyrecruitbackend.dto.OffreStageDTO;
import org.example.easyrecruitbackend.dto.PageResponse;
import org.example.easyrecruitbackend.entity.Domaine;
import org.example.easyrecruitbackend.service.OffreStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
public class OffreStageController {

    @Autowired
    private OffreStageService offreStageService;

    @PostMapping
    public ResponseEntity<OffreStageDTO> createOffre(@RequestBody OffreStageDTO offreStageDTO) {
        try {
            OffreStageDTO createdOffre = offreStageService.createOffre(offreStageDTO);
            return new ResponseEntity<>(createdOffre, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OffreStageDTO> updateOffre(@PathVariable Long id, @RequestBody OffreStageDTO offreStageDTO) {
        OffreStageDTO updatedOffre = offreStageService.updateOffre(id, offreStageDTO);
        if (updatedOffre != null) {
            return new ResponseEntity<>(updatedOffre, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{offreId}/candidatures")
    public ResponseEntity<CandidatureDTO> addCandidature(@RequestBody CandidatureDTO candidatureDTO) {
        CandidatureDTO savedCandidature = offreStageService.addCandidature(candidatureDTO);
        if (savedCandidature != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidature);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{offreId}/candidatures")
    public ResponseEntity<List<CandidatureDTO>> getCandidatures(@PathVariable Long offreId) {
        List<CandidatureDTO> candidatures = offreStageService.getCandidatures(offreId);
        if (candidatures != null) {
            return new ResponseEntity<>(candidatures, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<OffreStageDTO>> getAllOffres() {
        List<OffreStageDTO> offres = offreStageService.getAllOffres();
        if (offres != null && !offres.isEmpty()) {
            return new ResponseEntity<>(offres, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/mes-offres/{id}")
    public ResponseEntity<List<OffreStageDTO>> getOffresByRecruteur(@PathVariable Long id) {
        List<OffreStageDTO> offres = offreStageService.getOffresByRecruteur(id);
        if (offres != null) {
            return new ResponseEntity<>(offres, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OffreStageDTO> getOffreById(@PathVariable Long id) {
        OffreStageDTO offre = offreStageService.getOffreById(id);
        if (offre != null) {
            return new ResponseEntity<>(offre, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteOffre(@PathVariable Long id) {
        boolean isDeleted = offreStageService.deleteOffre(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<OffreStageDTO>> searchByTitre(@PathVariable String keyword) {
        List<OffreStageDTO> offres = offreStageService.searchByTitre(keyword);
        if (offres != null && !offres.isEmpty()) {
            return new ResponseEntity<>(offres, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/locations")
    public ResponseEntity<List<String>> getLocations() {
        List<String> locations = offreStageService.getLocations();
        if (locations != null && !locations.isEmpty()) {
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/paginated")
    public PageResponse<OffreStageDTO> getPaginatedOffres(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return offreStageService.getPaginatedOffres(page, size);
    }

    @GetMapping("/related")
    public ResponseEntity<List<OffreStageDTO>> getRelatedOffers(
            @RequestParam String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long excludeId
    ) {
        Domaine domain = category != null ? Domaine.valueOf(category) : null;
        List<OffreStageDTO> relatedOffers = offreStageService.searchRelatedOffers(keyword, category, location , excludeId);
        if (relatedOffers != null && !relatedOffers.isEmpty()) {
            return new ResponseEntity<>(relatedOffers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
