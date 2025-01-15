package org.example.easyrecruitbackend.repository;


import jakarta.transaction.Transactional;
import org.example.easyrecruitbackend.entity.Candidature;
import org.example.easyrecruitbackend.entity.EtatCandidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByOffreId(Long offreId);
    List<Candidature> findByCandidatId(Long candidatId);

    @Modifying
    @Transactional
    @Query("UPDATE Candidature c SET c.etat = :etat WHERE c.id = :id")
    int updateCandidatureStateById(@Param("id") Long id, @Param("etat") EtatCandidature etat);
    List<Candidature> findByEtat(EtatCandidature etat);


    List<Candidature> findTop5ByOrderByDateDesc();

    long countByEtat(EtatCandidature etat);
    @Query("SELECT c FROM Candidature c WHERE c.etat IN (:etats)")
    List<Candidature> findByEtats(@Param("etats") List<EtatCandidature> etats);

    @Query("SELECT new map(DAY(c.date) as day, c.date as date, COUNT(c) as count) " +
            "FROM Candidature c " +
            "WHERE c.offre.recruteur.id = :recruteurId " +
            "GROUP BY DAY(c.date), c.date " +
            "ORDER BY c.date DESC")
    List<Map<String, Object>> findApplicationsPerDay(@Param("recruteurId") Long recruteurId);



}
