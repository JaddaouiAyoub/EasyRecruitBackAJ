package org.example.easyrecruitbackend.repository;


import org.example.easyrecruitbackend.entity.Recruteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruteurRepository extends JpaRepository<Recruteur, Long> {
    Optional<Recruteur> findByEmail(String email);
    Optional<Recruteur> findByUsername(String username);

    Optional<Recruteur> findByUsernameAndPassword(String username, String password);
}