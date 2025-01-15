package org.example.easyrecruitbackend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Recruteur extends Utilisateur {


    @OneToMany(mappedBy = "recruteur", cascade = CascadeType.ALL)
    private List<OffreStage> tableauDeBord;

    private String companyName;
}
