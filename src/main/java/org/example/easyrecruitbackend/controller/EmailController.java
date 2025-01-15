package org.example.easyrecruitbackend.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.easyrecruitbackend.dto.EmailDTO;
import org.example.easyrecruitbackend.entity.EtatCandidature;

import org.example.easyrecruitbackend.service.CandidatureService;
import org.example.easyrecruitbackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final CandidatureService candidatureService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody EmailDTO emailDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            // Mettre à jour l'état de la candidature
            candidatureService.updateCandidatureStateByEmail(emailDTO.getTo(), EtatCandidature.INVITE_ENTRETIEN);

            emailService.sendStyledEmail(emailDTO);
            response.put("message", "Email sent successfully to " + emailDTO.getTo());
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            response.put("message", "Failed to send email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }


}
