package org.example.easyrecruitbackend.mapper;

import org.example.easyrecruitbackend.dto.CandidatDTO;
import org.example.easyrecruitbackend.entity.Candidat;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CandidatMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public CandidatMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Mapper Candidat à CandidatDTO
    public CandidatDTO toCandidatDTO(Candidat candidat) {
        return modelMapper.map(candidat, CandidatDTO.class);
    }

    // Mapper CandidatDTO à Candidat
    public Candidat toCandidatEntity(CandidatDTO candidatDTO) {
        return modelMapper.map(candidatDTO, Candidat.class);
    }

    // Configurer des mappages spécifiques si nécessaire
    public void configureMappings() {
        // Cette section est pour ignorer des propriétés spécifiques lors du mappage
        // Exemple d'ignorer la propriété 'id' lors du mappage de CandidatDTO à Candidat
        // modelMapper.typeMap(CandidatDTO.class, Candidat.class).addMappings(mapper -> {
        //     mapper.skip(CandidatDTO::getId); // Ignorer la propriété 'id'
        // });

        // Si vous souhaitez ignorer d'autres propriétés, vous pouvez ajouter des mappages similaires ici
    }
}
