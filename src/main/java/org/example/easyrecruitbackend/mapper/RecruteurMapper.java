package org.example.easyrecruitbackend.mapper;

import org.example.easyrecruitbackend.dto.RecruteurDTO;
import org.example.easyrecruitbackend.entity.Recruteur;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecruteurMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public RecruteurMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Mapper Recruteur à RecruteurDTO
    public RecruteurDTO toRecruteurDTO(Recruteur recruteur) {
        return modelMapper.map(recruteur, RecruteurDTO.class);
    }

    // Mapper RecruteurDTO à Recruteur
    public Recruteur toRecruteurEntity(RecruteurDTO recruteurDTO) {
        return modelMapper.map(recruteurDTO, Recruteur.class);
    }

    // Mettre à jour une entité Recruteur existante à partir d'un DTO
    public void updateRecruteurFromDTO(RecruteurDTO dto, Recruteur entity) {
        modelMapper.map(dto, entity);
    }

    // Configurer des mappages spécifiques si nécessaire
    public void configureMappings() {
        // Cette section est pour ignorer des propriétés spécifiques lors du mappage
        // Exemple d'ignorer la propriété 'id' lors du mappage de RecruteurDTO à Recruteur
        // modelMapper.typeMap(RecruteurDTO.class, Recruteur.class).addMappings(mapper -> {
        //     mapper.skip(RecruteurDTO::getId); // Ignorer la propriété 'id'
        // });

        // Si vous souhaitez ignorer d'autres propriétés, vous pouvez ajouter des mappages similaires ici
    }
}

