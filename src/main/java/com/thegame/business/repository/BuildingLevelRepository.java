package com.thegame.business.repository;

import com.thegame.business.model.BuildingLevel;
import com.thegame.business.model.BuildingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingLevelRepository extends JpaRepository<BuildingLevel, Long> {

    public BuildingLevel findBuildingLevelByBuildingLevelAndBuildingType(Long buildingLevel, BuildingType buildingType);

}
