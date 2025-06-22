package com.thegame.business.service;

import com.thegame.business.model.BuildingLevel;
import com.thegame.business.model.BuildingType;
import com.thegame.business.repository.BuildingLevelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BuildingLevelService {

    BuildingLevelRepository buildingLevelRepository;

    public BuildingLevelService(BuildingLevelRepository buildingLevelRepository) {
        this.buildingLevelRepository = buildingLevelRepository;
    }

    public BuildingLevel getBuildingLvlbyBuildingLvlAndBuildingType(Long buildingLevelNumber, BuildingType buildingType) {
        return buildingLevelRepository
                .findBuildingLevelByBuildingLevelAndBuildingType(buildingLevelNumber, buildingType)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Es konnte kein BuildingLevel für buildingLevelNumber %s und BuildingType %s gefunden werden.", buildingLevelNumber, buildingType)));
    }
}