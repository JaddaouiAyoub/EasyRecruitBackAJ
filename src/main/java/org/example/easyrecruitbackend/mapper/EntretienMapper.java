package org.example.easyrecruitbackend.mapper;

import org.example.easyrecruitbackend.dto.EntretienDTO;
import org.example.easyrecruitbackend.dto.QuestionDTO;
import org.example.easyrecruitbackend.entity.Entretien;

import java.util.List;
import java.util.stream.Collectors;

public class EntretienMapper {

    // Convert Entretien entity to DTO
    public static EntretienDTO toDTO(Entretien entretien) {
        if (entretien == null) return null;

        List<QuestionDTO> questionDTOs = null;
        if (entretien.getQuestions() != null) {
            questionDTOs = entretien.getQuestions().stream()
                    .map(question -> new QuestionDTO(
                            question.getId(),
                            question.getContenu() // Replace "getContenu" with the correct getter
                    ))
                    .collect(Collectors.toList());
        }

        return new EntretienDTO(
                entretien.getId(),
                questionDTOs,
                entretien.getOffre() != null ? entretien.getOffre().getId() : null
        );
    }

    // Convert DTO to Entretien entity
//    public static Entretien toEntity(EntretienDTO entretienDTO) {
//        if (entretienDTO == null) return null;
//
//        Entretien entretien = new Entretien();
//        entretien.setId(entretienDTO.getId());
//
//        if (entretienDTO.getQuestions() != null) {
//            List<Question> questions = entretienDTO.getQuestions().stream()
//                    .map(dto -> {
//                        Question question = new Question();
//                        question.setId(dto.getId());
//                        question.setContenu(dto.getContenu()); // Replace "setContenu" with the correct setter
//                        return question;
//                    })
//                    .collect(Collectors.toList());
//            entretien.setQuestions(questions);
//        }
//
//        // The "offre" association can be set if needed by fetching the OffreStage entity
//        return entretien;
//    }
}

