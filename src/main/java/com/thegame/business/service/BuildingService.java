package com.thegame.business.service;

import com.thegame.business.dto.ResponseDto;
import com.thegame.business.enums.ResourceType;
import com.thegame.business.model.BuildingLevel;
import com.thegame.business.repository.BuildingRepository;
import com.thegame.business.repository.ResourceByVillageResponse;
import com.thegame.business.utils.FileReader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BuildingService {

    private static final Logger logger = LoggerFactory.getLogger(BuildingService.class);


    private final BuildingRepository buildingRepository;
    private final ResourceService resourceService;
    private final BuildingLevelService buildingLevelService;

    public BuildingService(BuildingRepository buildingRepository, ResourceService resourceService, BuildingLevelService buildingLevelService) {
        this.buildingRepository = buildingRepository;
        this.resourceService = resourceService;
        this.buildingLevelService = buildingLevelService;
    }

    public List<BuildingRepository.BuildingsByVillageIdResponse> getBuildingsByVillageId(Long villageId) {
        return buildingRepository.getBuildingsByVillageId(villageId);
    }

    /**
     * Startet die Erhöhung des Gebäude-Levels.
     * Diese Methode zieht die Resourcen für das Gebäude Update ab und setzt das Gebäude Level hoch.
     * Die UpdateTime wird entsprechend der Bauzeit in die Zukunft gesetzt.
     */
    // rollbackFor standard behaviour: only for RunTimeExceptions and Errors
    @Transactional(rollbackFor = { NullPointerException.class, IllegalStateException.class, IOException.class, URISyntaxException.class })
    public ResponseEntity<List<ResponseDto>> buildingUpgrade(Long villageId, Long buildingTypeId) throws IOException, URISyntaxException {
        long nextBuildingLevel = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId).getBuildingLevel() + 1;
        BuildingLevel buildingLevel = buildingLevelService
                .getBuildingLvlbyBuildingLvlAndBuildingType(nextBuildingLevel, new com.thegame.business.model.BuildingType(buildingTypeId));

        List<ResponseDto> missingResources = resourceService.decreaseRessBeforeBuildAction(villageId, buildingLevel);

        if (!missingResources.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(missingResources);
        } else {
            updateBuildingLvl(villageId, buildingTypeId, buildingLevel.getBuildingTime());
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    private List<ResponseDto> checkForMissingResources(Float stoneAfterLvlIncrease, Float woodAfterLvlIncrease) {
        List<ResponseDto> responseDtos = new ArrayList<>();
        if (stoneAfterLvlIncrease < 0) {
          responseDtos.add(new ResponseDto(ResourceType.STONE.getValue(), "not enough"));
        }
        if (woodAfterLvlIncrease < 0) {
            responseDtos.add(new ResponseDto(ResourceType.WOOD.getValue(), "not enough"));
        }
        return responseDtos;
    }

    private JSONObject getBuildingUpdateDetails(Long buildingTypeId, Long buildingLevel, String levelDetail) throws IOException, URISyntaxException {
        String jsonContent = FileReader.readResourceFile("buildings.json");
        JSONObject buildingJsonObject = new JSONObject(jsonContent);
        JSONObject building = buildingJsonObject.getJSONObject(buildingTypeId.toString());
        JSONObject levels = building.getJSONObject("levels");
        JSONObject levelDetails = levels.getJSONObject(buildingLevel.toString());
        return levelDetails.getJSONObject(levelDetail);
    }

    private ResourceByVillageResponse retrieveResByVillageId (List<ResourceByVillageResponse> resourcesByVillageId, ResourceType resourceType) {
        return resourcesByVillageId.stream()
                .filter(res -> res.getResourceTypeId().equals(resourceType.getValue()) && res.getResourceAtUpdateTime() != null)
                .findFirst().orElseThrow(() -> new IllegalStateException(String.format("%s resource not found", resourceType.getFullName())));
    }

    private Float retrieveResourcesAfterLvlIncrease(ResourceByVillageResponse resObj, Long resRequired) {
        if (resObj == null || resRequired == null) {
            logger.debug("Retrieving of Resources after Lvl Increase does not work with resRequired: {} and resObj: {}", resRequired, resObj);
            throw new IllegalArgumentException("Retrieving of Resources after Lvl Increase does not work! Rollback of transaction done");

        }
        LocalDateTime now = LocalDateTime.now();
        long timeDiffInSecs = Duration.between(resObj.getUpdateTime(), now).getSeconds();
        float resIncomePerSec = resObj.getResourceIncome().floatValue() / 3600;
        float resNow = resObj.getResourceAtUpdateTime() + resIncomePerSec * timeDiffInSecs;
        return resNow - resRequired.floatValue();
    }

    private void updateBuildingLvl(Long villageId, Long buildingTypeId, Long updateDuration) {
        var updatedBuilding = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId);
        if (updatedBuilding != null) {
            updatedBuilding.setBuildingLevel(updatedBuilding.getBuildingLevel() + 1);
            updatedBuilding.setUpdateTime(LocalDateTime.now().plusSeconds(updateDuration));
            buildingRepository.save(updatedBuilding);
        }
    }
}