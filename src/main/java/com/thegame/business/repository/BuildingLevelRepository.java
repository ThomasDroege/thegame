package com.thegame.business.repository;

import com.thegame.business.model.BuildingLevel;
import com.thegame.business.model.BuildingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuildingLevelRepository extends JpaRepository<BuildingLevel, Long> {

    Optional<BuildingLevel> findBuildingLevelByBuildingLevelAndBuildingType(Long buildingLevel, BuildingType buildingType);

}
