package org.example.easyrecruitbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntretienDTO {
    private Long id;
    private List<QuestionDTO> questions;
    private Long offreId;
}