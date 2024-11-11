package com.thegame.business.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "buildings", schema = "data")
public class Building {

    @Id
    @Column(name = "building_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long buildingId;

    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;

    @ManyToOne
    @JoinColumn(name = "building_type_id")
    private BuildingType buildingType;

    @Column(name = "building_level")
    private Long buildingLevel;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public Long getBuildingLevel() {
        return buildingLevel;
    }

    public void setBuildingLevel(Long buildingLevel) {
        this.buildingLevel = buildingLevel;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}