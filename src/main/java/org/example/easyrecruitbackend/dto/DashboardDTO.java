package org.example.easyrecruitbackend.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Data
public class DashboardDTO {
    private int totalJobOffers; // Total des offres d'emploi actives
    private int totalCandidates; // Nombre total de candidats
    private int totalInterviews; // Nombre total d'entretiens planifiés
    private List<String> recentActivities; // Activités récentes
    private Map<Integer, Date> applicationsPerDay; // Applications par mois
    private List<CategoryData> jobOffersByCategory; // Offres par catégorie

    // Méthode pour filtrer les clés nulles et les valeurs nulles dans la carte
    public void setApplicationsPerDay(Map<Integer, Date> applicationsPerDay) {
        if (applicationsPerDay != null) {
            Map<Integer, Date> filteredMap = new HashMap<>();

            // Filtrage des entrées nulles
            for (Map.Entry<Integer, Date> entry : applicationsPerDay.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    filteredMap.put(entry.getKey(), entry.getValue());
                }
            }

            this.applicationsPerDay = filteredMap;
        } else {
            this.applicationsPerDay = new HashMap<>(); // Valeur par défaut si la carte est null
        }
    }

    @Data
    public static class CategoryData {
        private String categoryName; // Nom de la catégorie
        private double count; // Nombre d'offres dans cette catégorie
    }
}
