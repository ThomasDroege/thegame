package com.thegame.business.service;

import com.thegame.business.dto.ResourceDto;
import com.thegame.business.dto.ResourceUpdateRequestDTO;
import com.thegame.business.dto.ResponseDto;
import com.thegame.business.enums.BuildingType;
import com.thegame.business.enums.ResourceType;
import com.thegame.business.repository.BuildingRepository;
import com.thegame.business.repository.ResourceRepository;
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

    public BuildingService(BuildingRepository buildingRepository, ResourceService resourceService) {
        this.buildingRepository = buildingRepository;
        this.resourceService = resourceService;
    }

    public List<BuildingRepository.BuildingsByVillageIdResponse> getBuildingsByVillageId(Long villageId) {
        return buildingRepository.getBuildingsByVillageId(villageId);
    }

    /**
     * Startet die Erhöhung des Gebäude-Levels.
     *
     * Diese Methode zieht die Resourcen für das Gebäude Update ab und setzt das Gebäude Level hoch.
     * Die UpdateTime wird entsprechend der Bauzeit in die Zukunft gesetzt.
     */
    // rollbackFor standard behaviour: only for RunTimeExceptions and Errors
    @Transactional(rollbackFor = { NullPointerException.class, IllegalStateException.class, IOException.class, URISyntaxException.class })
    public ResponseEntity<List<ResponseDto>> buildingUpgrade(Long villageId, Long buildingTypeId) throws IOException, URISyntaxException {
        List<ResourceRepository.ResourceByVillageResponse> resourcesByVillageId = resourceService.getResourcesByVillageId(villageId);

        resourceService.aggregateAndUpdateResources(resourcesByVillageId, villageId);
        resourcesByVillageId = resourceService.getResourcesByVillageId(villageId);

        HashMap<String, Long> updateCostsAndDurationMap = getNextBuildingLvlInfos(villageId, buildingTypeId);

        ResourceRepository.ResourceByVillageResponse stoneByVillageId = retrieveResByVillageId(resourcesByVillageId, ResourceType.STONE);
        Float stoneAfterLvlIncrease = retrieveResourcesAfterLvlIncrease(stoneByVillageId, updateCostsAndDurationMap.get("stoneUpdateCosts"));

        ResourceRepository.ResourceByVillageResponse woodByVillageId = retrieveResByVillageId(resourcesByVillageId, ResourceType.WOOD);
        Float woodAfterLvlIncrease = retrieveResourcesAfterLvlIncrease(woodByVillageId, updateCostsAndDurationMap.get("woodUpdateCosts"));

        List<ResponseDto> missingResources = checkForMissingResources(stoneAfterLvlIncrease, woodAfterLvlIncrease);
        if (!missingResources.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(missingResources);
        } else {
            decreaseResByUpdateCosts(villageId, stoneAfterLvlIncrease.longValue(), stoneByVillageId.getResourceIncome(), stoneByVillageId.getUpdateTime(),
                    woodAfterLvlIncrease.longValue(), woodByVillageId.getResourceIncome(), woodByVillageId.getUpdateTime());
            addNewResRowIfBuildingChangesResIncome(buildingTypeId, updateCostsAndDurationMap.get("nextBuildingLevel"), villageId);
            updateBuildingLvl(villageId, buildingTypeId, updateCostsAndDurationMap.get("updateDuration"));

            // Line for Test purpose of transaction rollback
            //  stoneAfterLvlIncrease = retrieveResourcesAfterLvlIncrease(stoneByVillageId, updateCostsAndDurationMap.get("stoeUpdateCosts"));
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    public HashMap<String, Long> getNextBuildingLvlInfos(Long villageId, Long buildingTypeId) throws IOException, URISyntaxException {
        long nextBuildingLevel = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId).getBuildingLevel() + 1;
        JSONObject updateCosts = getBuildingUpdateDetails(buildingTypeId, nextBuildingLevel, "updatecosts");
        HashMap<String, Long> updateCostsAndDurationMap = new HashMap<>();
        updateCostsAndDurationMap.put("nextBuildingLevel", nextBuildingLevel);
        updateCostsAndDurationMap.put("stoneUpdateCosts", updateCosts.getLong(ResourceType.STONE.getFullName()));
        updateCostsAndDurationMap.put("woodUpdateCosts", updateCosts.getLong(ResourceType.WOOD.getFullName()));
        updateCostsAndDurationMap.put("updateDuration", getBuildingUpdateDetails(buildingTypeId, nextBuildingLevel, "buildingtime").optLong("seconds"));
        return updateCostsAndDurationMap;
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

    private ResourceRepository.ResourceByVillageResponse retrieveResByVillageId (List<ResourceRepository.ResourceByVillageResponse> resourcesByVillageId, ResourceType resourceType) {
        return resourcesByVillageId.stream()
                .filter(res -> res.getResourceTypeId().equals(resourceType.getValue()) && res.getResourceAtUpdateTime() != null)
                .findFirst().orElseThrow(() -> new IllegalStateException(String.format("%s resource not found", resourceType.getFullName())));
    }

    private Float retrieveResourcesAfterLvlIncrease(ResourceRepository.ResourceByVillageResponse resObj, Long resRequired) {
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

    private void decreaseResByUpdateCosts(Long villageId, Long stoneAfterLvlIncrease, Long stoneIncome, LocalDateTime stoneUpdateTime,
                                          Long woodAfterLvlIncrease, Long woodIncome, LocalDateTime woodResUpdateTime) {
        ResourceDto stoneRes = new ResourceDto(ResourceType.STONE.getValue(), stoneAfterLvlIncrease, stoneIncome, stoneUpdateTime);
        ResourceDto woodRes = new ResourceDto(ResourceType.WOOD.getValue(), woodAfterLvlIncrease, woodIncome, woodResUpdateTime);
        ResourceUpdateRequestDTO resourceUpdateRequestDTO = new ResourceUpdateRequestDTO(villageId, List.of(stoneRes, woodRes));
        resourceService.updateResourcesByVillageId(resourceUpdateRequestDTO);
    }

    private void addNewResRowIfBuildingChangesResIncome (Long buildingTypeId, Long nextBuildingLevel, Long villageId) throws IOException, URISyntaxException {
        List<Long> resourceIncomeDependentBuildingTypeIds = List.of(BuildingType.MILL.getValue(), BuildingType.LUMBERJACK.getValue(), BuildingType.MASON.getValue(), BuildingType.IRON_MINE.getValue());
        if (resourceIncomeDependentBuildingTypeIds.contains(buildingTypeId)) {
            JSONObject resourceIncome = getBuildingUpdateDetails(buildingTypeId, nextBuildingLevel, "income");
            Long resourceTypeId = resourceIncome.optLong("resourceTypeId");
            Long resIncome = resourceIncome.optLong("value");
            resourceService.insertResource(villageId, resourceTypeId, null, resIncome);
        }
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