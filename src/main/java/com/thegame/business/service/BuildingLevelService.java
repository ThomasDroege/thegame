package com.thegame.business.service;

import com.thegame.business.model.BuildingLevel;
import com.thegame.business.model.BuildingType;
import com.thegame.business.repository.BuildingLevelRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
public class BuildingLevelService {

    BuildingLevelRepository buildingLevelRepository;

    public BuildingLevelService(BuildingLevelRepository buildingLevelRepository) {
        this.buildingLevelRepository = buildingLevelRepository;
    }

    //ToDo: Error Handling und Testen
    public BuildingLevel getBuildingLvlbyBuildingLvlAndBuildingType(Long buildingLevel, BuildingType buildingType) {
        return buildingLevelRepository
                .findBuildingLevelByBuildingLevelAndBuildingType(buildingLevel, buildingType);
    }


}
