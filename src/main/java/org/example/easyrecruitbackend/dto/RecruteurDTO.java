package org.example.easyrecruitbackend.dto;

import lombok.Data;
import org.example.easyrecruitbackend.entity.Recruteur;
import org.example.easyrecruitbackend.entity.Role;

@Data
public class RecruteurDTO {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private String companyName;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilPic;  // Ajout du champ photoProfil

    // Method to convert Recruteur entity to RecruteurDTO
    public static RecruteurDTO toDTO(Recruteur recruteur) {
        RecruteurDTO dto = new RecruteurDTO();
        dto.setId(recruteur.getId());
        dto.setUsername(recruteur.getUsername()); // Assurez-vous que votre entité Recruteur a un champ `username`
        dto.setFirstName(recruteur.getFirstName()); // Utiliser `getNom()` pour obtenir le prénom du recruteur
        dto.setLastName(recruteur.getLastName()); // Utiliser `getPrenom()` pour obtenir le nom de famille
        dto.setEmail(recruteur.getEmail());
        dto.setPhoneNumber(recruteur.getPhoneNumber()); // Ajout de la méthode pour récupérer le numéro de téléphone
        dto.setProfilPic(recruteur.getProfilPic()); // Assurez-vous que votre entité Recruteur a un champ `photoProfil`
        dto.setRole(recruteur.getRole()); // Assurez-vous que le rôle est également récupéré
        dto.setCompanyName(recruteur.getCompanyName()); // Si ce champ existe dans l'entité, vous devez le récupérer ici
        return dto;
    }
}
