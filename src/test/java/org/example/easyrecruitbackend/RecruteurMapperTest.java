package org.example.easyrecruitbackend;

import org.example.easyrecruitbackend.dto.RecruteurDTO;
import org.example.easyrecruitbackend.entity.Recruteur;
import org.example.easyrecruitbackend.entity.Role;
import org.example.easyrecruitbackend.mapper.RecruteurMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RecruteurMapperTest {

    @Autowired
    private RecruteurMapper recruteurMapper;

    private Recruteur recruteur;
    private RecruteurDTO recruteurDTO;

    @BeforeEach
    public void setUp() {
        // Initialiser un objet Recruteur
        recruteur = new Recruteur();
        recruteur.setId(1L);
        recruteur.setEmail("recruteur@example.com");
        recruteur.setPassword("password123");
        recruteur.setRole(Role.RECRUTEUR); // Remplacez par un rôle valide si vous en avez défini
        recruteur.setCompanyName("Company XYZ");

        // Initialiser un objet RecruteurDTO
        recruteurDTO = new RecruteurDTO();
        recruteurDTO.setId(1L);
        recruteurDTO.setEmail("recruteur@example.com");
        recruteurDTO.setPassword("password123");
        recruteurDTO.setRole(Role.RECRUTEUR); // Remplacez par un rôle valide si vous en avez défini
        recruteurDTO.setCompanyName("Company XYZ");
    }

    @Test
    public void testToRecruteurDTO() {
        // Tester le mappage de Recruteur à RecruteurDTO
        RecruteurDTO result = recruteurMapper.toRecruteurDTO(recruteur);

        // Vérifier que toutes les propriétés sont bien mappées
        assertEquals(recruteur.getId(), result.getId());
        assertEquals(recruteur.getEmail(), result.getEmail());
        assertEquals(recruteur.getPassword(), result.getPassword());
        assertEquals(recruteur.getRole(), result.getRole());
        assertEquals(recruteur.getCompanyName(), result.getCompanyName());
    }

    @Test
    public void testToRecruteurEntity() {
        // Tester le mappage de RecruteurDTO à Recruteur
        Recruteur result = recruteurMapper.toRecruteurEntity(recruteurDTO);

        // Vérifier que toutes les propriétés sont bien mappées
        assertEquals(recruteurDTO.getId(), result.getId());
        assertEquals(recruteurDTO.getEmail(), result.getEmail());
        assertEquals(recruteurDTO.getPassword(), result.getPassword());
        assertEquals(recruteurDTO.getRole(), result.getRole());
        assertEquals(recruteurDTO.getCompanyName(), result.getCompanyName());
    }
}
