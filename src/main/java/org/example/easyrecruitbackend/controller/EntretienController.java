package org.example.easyrecruitbackend.controller;

import org.example.easyrecruitbackend.dto.EntretienDTO;
import org.example.easyrecruitbackend.service.EntretienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entretiens")
public class EntretienController {

    @Autowired
    private EntretienService entretienService;


    @PostMapping("/create/{offreId}")
    public ResponseEntity<EntretienDTO> createEntretienForOffre(@PathVariable Long offreId, @RequestBody List<String> questions) {
        try {
            EntretienDTO entretienDTO = entretienService.createEntretienForOffre(offreId, questions);
            return ResponseEntity.ok(entretienDTO);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null); // Return a null body with bad request status
        }
    }

    @GetMapping("/{id}")
    public EntretienDTO getEntretienById(@PathVariable Long id) {
        return entretienService.getEntretienById(id);
    }

    @GetMapping("/by-offre/{offreId}")
    public EntretienDTO getEntretienByOffreId(@PathVariable Long offreId) {
        return entretienService.getEntretienByOffreId(offreId);
    }

    @GetMapping("/exists/{offreId}/{candidatId}")
    public boolean accessToInterview(@PathVariable Long offreId,@PathVariable Long candidatId){
        return entretienService.interviewExists(offreId);
    }


    @GetMapping("/exists/{offreId}")
    public boolean Iterviewexists(@PathVariable Long offreId){
        return entretienService.interviewExists(offreId);
}

}
