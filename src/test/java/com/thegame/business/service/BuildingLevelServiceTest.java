package com.thegame.business.service;

import com.thegame.business.model.BuildingLevel;
import com.thegame.business.model.BuildingType;
import com.thegame.business.repository.BuildingLevelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BuildingLevelServiceTest {

    private final BuildingLevelRepository buildingLevelRepository = Mockito.mock(BuildingLevelRepository.class);

    private final BuildingLevelService buildingLevelService = new BuildingLevelService(buildingLevelRepository);

    @Test
    public void getBuildingLvlbyBuildingLvlAndBuildingTypeFound() {
        //GIVEN
        final Long buildingLevelNumber = 5L;
        final Long buildingTypeId = 1L;
        final BuildingType buildingType = new BuildingType(buildingTypeId);
        final BuildingLevel buildingLevel =new BuildingLevel();
        buildingLevel.setBuildingLevel(buildingLevelNumber);
        buildingLevel.setBuildingType(buildingType);


        when(buildingLevelRepository.findBuildingLevelByBuildingLevelAndBuildingType(buildingLevelNumber, buildingType)).thenReturn(Optional.of(buildingLevel));

        // EXPECTED
        assertThatCode(() -> buildingLevelService
                .getBuildingLvlbyBuildingLvlAndBuildingType(buildingLevelNumber, buildingType))
                .doesNotThrowAnyException();
    }

    @Test
    public void getBuildingLvlbyBuildingLvlAndBuildingTypeThrowsException() {
        //GIVEN
        final Long buildingLevelNumber = 5L;
        final Long buildingTypeId = 1L;
        final BuildingType buildingType = new BuildingType(buildingTypeId);
        final BuildingLevel buildingLevel =new BuildingLevel();
        buildingLevel.setBuildingLevel(buildingLevelNumber);
        buildingLevel.setBuildingType(buildingType);


        when(buildingLevelRepository.findBuildingLevelByBuildingLevelAndBuildingType(buildingLevelNumber, buildingType)).thenReturn(Optional.empty());

        // EXPECTED
        assertThatThrownBy(() -> buildingLevelService
                .getBuildingLvlbyBuildingLvlAndBuildingType(buildingLevelNumber, buildingType))
                .hasMessage(String.format("Es konnte kein BuildingLevel für buildingLevelNumber %s und BuildingType %s gefunden werden.", buildingLevelNumber, buildingType))
                .isInstanceOf(EntityNotFoundException.class);
    }
}