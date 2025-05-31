package com.thegame.business.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "buildingLevel", schema = "data")
public class BuildingLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long buildingLevelId;

    @Column(nullable = false)
    private Long buildingLevel;

    @Column(nullable = false)
    private Long buildingTime;

    @Column(nullable = false)
    private Long updateCostStone;

    @Column(nullable = false)
    private Long updateCostWood;

    @Column(nullable = false)
    private Long buildingLevelValue;

    @ManyToOne
    @JoinColumn(name = "building_level_effect_id")
    private BuildingLevelEffect buildingLevelEffect;

    @ManyToOne
    @JoinColumn(name = "building_type_id")
    private BuildingType buildingType;

    public Long getBuildingLevelId() {
        return buildingLevelId;
    }

    public void setBuildingLevelId(Long buildingLevelId) {
        this.buildingLevelId = buildingLevelId;
    }

    public Long getBuildingLevel() {
        return buildingLevel;
    }

    public void setBuildingLevel(Long buildingLevel) {
        this.buildingLevel = buildingLevel;
    }

    public Long getBuildingTime() {
        return buildingTime;
    }

    public void setBuildingTime(Long buildingTime) {
        this.buildingTime = buildingTime;
    }

    public Long getUpdateCostStone() {
        return updateCostStone;
    }

    public void setUpdateCostStone(Long updateCostStone) {
        this.updateCostStone = updateCostStone;
    }

    public Long getUpdateCostWood() {
        return updateCostWood;
    }

    public void setUpdateCostWood(Long updateCostWood) {
        this.updateCostWood = updateCostWood;
    }

    public Long getBuildingLevelValue() {
        return buildingLevelValue;
    }

    public void setBuildingLevelValue(Long buildingLevelValue) {
        this.buildingLevelValue = buildingLevelValue;
    }

    public BuildingLevelEffect getBuildingLevelEffect() {
        return buildingLevelEffect;
    }

    public void setBuildingLevelEffect(BuildingLevelEffect buildingLevelEffect) {
        this.buildingLevelEffect = buildingLevelEffect;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
}
