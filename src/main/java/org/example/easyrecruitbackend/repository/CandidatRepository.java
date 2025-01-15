package org.example.easyrecruitbackend.repository;


import org.example.easyrecruitbackend.entity.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    Optional<Candidat> findByEmail(String email);
    Optional<Candidat> findByUsernameAndPassword(String username,String password);

    Optional<Candidat> findByUsername(String username);
}



