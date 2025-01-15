package org.example.easyrecruitbackend.service;

import org.example.easyrecruitbackend.dto.DashboardDTO;
import org.example.easyrecruitbackend.dto.OffreStageDTO;
import org.example.easyrecruitbackend.entity.Candidature;
import org.example.easyrecruitbackend.entity.EtatCandidature;
import org.example.easyrecruitbackend.entity.OffreStage;
import org.example.easyrecruitbackend.repository.CandidatureRepository;
import org.example.easyrecruitbackend.repository.OffreStageRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class DashboardService {

    private final OffreStageRepository offreStageRepository;
    private final CandidatureRepository candidatureRepository;

    public DashboardService(OffreStageRepository offreStageRepository, CandidatureRepository candidatureRepository) {
        this.offreStageRepository = offreStageRepository;
        this.candidatureRepository = candidatureRepository;
    }

    public DashboardDTO getDashboardData(Long recruteurId) {
        DashboardDTO dashboardDTO = new DashboardDTO();

        // Total des offres d'emploi pour le recruteur
        List<OffreStage> offres = offreStageRepository.findByRecruteurId(recruteurId);
        dashboardDTO.setTotalJobOffers(offres.size());

        // Nombre total de candidats ayant postulé aux offres de ce recruteur
        Set<Long> candidatsId = offres.stream()
                .flatMap(offre -> offre.getCandidatures().stream())
                .map(candidature -> candidature.getCandidat().getId())
                .collect(Collectors.toSet());
        dashboardDTO.setTotalCandidates(candidatsId.size());

        // Nombre total d'entretiens planifiés pour les candidats invités ou passés
        List<Candidature> candidatures = candidatureRepository.findByEtats(
                Arrays.asList(EtatCandidature.INVITE_ENTRETIEN, EtatCandidature.PASSE_ENTRETIEN)
        );
        dashboardDTO.setTotalInterviews(candidatures.size());

        // Activités récentes
        dashboardDTO.setRecentActivities(
                candidatureRepository.findTop5ByOrderByDateDesc().stream()
                        .map(candidature -> "Nouvelle candidature de " + candidature.getCandidat().getLastName() + " " + candidature.getCandidat().getFirstName() + " pour l'offre " + candidature.getOffre().getTitre())
                        .collect(Collectors.toList())
        );

        // Applications par jour - récupérées de la base de données
        List<Map<String, Object>> applicationsPerDayData = candidatureRepository.findApplicationsPerDay(recruteurId);
        System.out.println("Applications per day data: " + applicationsPerDayData); // Vérification
        Map<Integer, Date> applicationsPerDay = new LinkedHashMap<>();
        for (Map<String, Object> data : applicationsPerDayData) {
            Integer day = (Integer) data.get("day");
            Date date = (Date) data.get("date");
            Integer count = ((Long) data.get("count")).intValue();

            // Vous pouvez choisir de garder la date ou de l'utiliser autrement selon votre besoin
            applicationsPerDay.put(day, date);
        }
        dashboardDTO.setApplicationsPerDay(applicationsPerDay);

        // Statistiques par catégorie d'offres d'emploi
        List<DashboardDTO.CategoryData> jobOffersByCategoryList = offres.stream()
                .filter(offre -> offre.getRecruteur().getId().equals(recruteurId))
                .collect(Collectors.groupingBy(OffreStage::getDomaine, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> {
                    long totalOffers = offres.stream()
                            .filter(offre -> offre.getRecruteur().getId().equals(recruteurId))
                            .count();

                    DashboardDTO.CategoryData categoryData = new DashboardDTO.CategoryData();
                    categoryData.setCategoryName(String.valueOf(entry.getKey()));

                    // Calcul du pourcentage
                    double percentage = (entry.getValue() * 100.0) / totalOffers;
                    categoryData.setCount(percentage);

                    return categoryData;
                })
                .collect(Collectors.toList());

        // Ajouter un élément par défaut si aucune catégorie n'est trouvée
        if (jobOffersByCategoryList.isEmpty()) {
            DashboardDTO.CategoryData defaultCategoryData = new DashboardDTO.CategoryData();
            defaultCategoryData.setCategoryName("Aucun domaine");
            defaultCategoryData.setCount(0);
            jobOffersByCategoryList.add(defaultCategoryData);
        }

        dashboardDTO.setJobOffersByCategory(jobOffersByCategoryList);

        return dashboardDTO;
    }

}
