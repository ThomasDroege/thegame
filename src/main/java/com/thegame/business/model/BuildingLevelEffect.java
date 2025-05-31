package com.thegame.business.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "buildingLevelEffect", schema = "data")
public class BuildingLevelEffect {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long buildingLevelEffectId;

    @Column(nullable = false)
    private String buildingLevelEffectName;

    public Long getBuildingLevelEffectId() {
        return buildingLevelEffectId;
    }

    public void setBuildingLevelEffectId(Long buildingLevelOutcomeId) {
        this.buildingLevelEffectId = buildingLevelOutcomeId;
    }

    public String getBuildingLevelEffectName() {
        return buildingLevelEffectName;
    }

    public void setBuildingLevelEffectName(String buildingLevelEffectName) {
        this.buildingLevelEffectName = buildingLevelEffectName;
    }
}
