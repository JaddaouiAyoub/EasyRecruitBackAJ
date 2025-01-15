package org.example.easyrecruitbackend.dto;

import lombok.Data;
import org.example.easyrecruitbackend.entity.Role;

@Data
public class CandidatDTO {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private String cvUrl;

    private String username;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String linkedin;
    private String profilPic;  // Ajout du champ photoProfil

}

