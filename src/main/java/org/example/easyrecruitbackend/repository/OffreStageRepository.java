package org.example.easyrecruitbackend.repository;

import org.example.easyrecruitbackend.dto.DashboardDTO;
import org.example.easyrecruitbackend.entity.Domaine;
import org.example.easyrecruitbackend.entity.Entretien;
import org.example.easyrecruitbackend.entity.OffreStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OffreStageRepository extends JpaRepository<OffreStage, Long> {
    @Query("SELECT o FROM OffreStage o JOIN FETCH o.candidatures WHERE o.id = :offreId")
    Optional<OffreStage> findByIdWithCandidatures(@Param("offreId") Long offreId);

    // Rechercher par titre contenant un mot clé
    List<OffreStage> findByTitreContainingOrDescriptionContaining(String titre, String description);

    // Rechercher par domaine
    List<OffreStage> findByDomaine(String domaine);

    // Rechercher par recruteur
    List<OffreStage> findByRecruteurId(Long recruteurId);

    Page<OffreStage> findAll(Pageable pageable);
    Page<OffreStage> findByTitreContainingOrDomaineOrLocation(String keyword, Domaine category, String location, Pageable pageable);

    @Query("SELECT o.entretien FROM OffreStage o WHERE o.id = :offreId")
    Entretien findEntretienByOffreId(Long offreId);
}
