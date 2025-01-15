package org.example.easyrecruitbackend.service;

import lombok.AllArgsConstructor;
import org.example.easyrecruitbackend.dto.CandidatureDTO;
import org.example.easyrecruitbackend.dto.OffreStageDTO;
import org.example.easyrecruitbackend.dto.PageResponse;
import org.example.easyrecruitbackend.entity.*;
import org.example.easyrecruitbackend.mapper.CandidatureMapper;
import org.example.easyrecruitbackend.mapper.OffreStageMapper;
import org.example.easyrecruitbackend.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OffreStageService {

    private final OffreStageRepository offreStageRepository;
    private final RecruteurRepository recruteurRepository;
    private final CandidatureRepository candidatureRepository;
    private final CandidatRepository candidatRepository;
    private final CandidatureMapper candidatureMapper;

    public OffreStageDTO createOffre(OffreStageDTO offreStageDTO) {
        Recruteur recruteur = recruteurRepository.findById(offreStageDTO.getRecruteurDTO().getId())
                .orElseThrow(() -> new RuntimeException("Recruteur not found"));
        OffreStage offreStage = OffreStageMapper.toEntity(offreStageDTO, recruteur,null);
        return OffreStageMapper.toDTO(offreStageRepository.save(offreStage));
    }

    public OffreStageDTO updateOffre(Long id, OffreStageDTO offreStageDTO) {
        return offreStageRepository.findById(id)
                .map(offre -> {
                    offre.setTitre(offreStageDTO.getTitre());
                    offre.setDescription(offreStageDTO.getDescription());
                    offre.setDatePublication(offreStageDTO.getDatePublication());
                    offre.setDomaine(Domaine.valueOf(offreStageDTO.getDomaine()));
                    offre.setPhoto(offreStageDTO.getPhoto());
                    return OffreStageMapper.toDTO(offreStageRepository.save(offre));
                })
                .orElse(null);
    }

    public CandidatureDTO addCandidature(CandidatureDTO candidatureDTO) {
        Optional<OffreStage> offre = offreStageRepository.findById(candidatureDTO.getOffreId());
        Optional<Candidat> candidat = candidatRepository.findById(candidatureDTO.getCandidatId());
        if (offre.isPresent() && candidat.isPresent()) {
            Candidature candidature = candidatureMapper.toEntity(candidatureDTO, candidat.get(), offre.get());
            return candidatureMapper.toDto(candidatureRepository.save(candidature));
        }
        return null;
    }

    public List<CandidatureDTO> getCandidatures(Long offreId) {
        return offreStageRepository.findByIdWithCandidatures(offreId)
                .map(offre -> offre.getCandidatures().stream()
                        .map(candidatureMapper::toDto)
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    public List<OffreStageDTO> getAllOffres() {
        return offreStageRepository.findAll().stream()
                .map(OffreStageMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<OffreStageDTO> getOffresByRecruteur(Long id) {
        return offreStageRepository.findByRecruteurId(id).stream()
                .map(OffreStageMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OffreStageDTO getOffreById(Long id) {
        return offreStageRepository.findById(id)
                .map(OffreStageMapper::toDTO)
                .orElse(null);
    }

    public boolean deleteOffre(Long id) {
        try {
            offreStageRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<OffreStageDTO> searchByTitre(String keyword) {
        return offreStageRepository.findByTitreContainingOrDescriptionContaining(keyword, keyword).stream()
                .map(OffreStageMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<String> getLocations() {
        return offreStageRepository.findAll().stream()
                .map(OffreStage::getLocation)
                .distinct()
                .collect(Collectors.toList());
    }

//    public List<OffreStageDTO> getPaginatedOffres(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<OffreStage> paginatedOffres = offreStageRepository.findAll(pageable);
//        return paginatedOffres.stream()
//                .map(OffreStageMapper::toDTO)
//                .collect(Collectors.toList());
//    }

    public PageResponse<OffreStageDTO> getPaginatedOffres(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OffreStage> paginatedOffres = offreStageRepository.findAll(pageable);

        List<OffreStageDTO> offreStageDTOs = paginatedOffres.getContent()
                .stream()
                .map(OffreStageMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                offreStageDTOs,                     // Liste des données paginées
                paginatedOffres.getTotalPages(),    // Nombre total de pages
                paginatedOffres.getTotalElements()  // Nombre total d'éléments
        );
    }

    public List<OffreStageDTO> searchRelatedOffers(String keyword, String category, String location, Long excludeId) {
        Pageable pageable = PageRequest.of(0, 4); // Limite de résultats
        Domaine domain = category != null ? Domaine.valueOf(category.toUpperCase()) : null;

        // Rechercher les offres correspondant aux critères
        Page<OffreStage> page = offreStageRepository.findByTitreContainingOrDomaineOrLocation(keyword, domain, location, pageable);

        // Exclure l'offre avec l'ID `excludeId` si elle est présente
        return page.getContent().stream()
                .filter(offer -> !offer.getId().equals(excludeId)) // Filtrer l'ID
                .map(OffreStageMapper::toDTO)
                .collect(Collectors.toList());
    }


}
