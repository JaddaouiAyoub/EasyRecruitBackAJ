package org.example.easyrecruitbackend.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String roleName; // Nom du rôle
    private String profilPic;

}

