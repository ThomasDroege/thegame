package com.thegame.business.service;

import com.thegame.business.dto.ResourceDto;
import com.thegame.business.dto.ResourceUpdateRequestDTO;
import com.thegame.business.enums.BuildingType;
import com.thegame.business.enums.ResourceType;
import com.thegame.business.repository.BuildingRepository;
import com.thegame.business.repository.ResourceRepository;
import com.thegame.business.utils.FileReader;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BuildingService {

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
    public ResponseEntity<String> buildingUpgrade(Long villageId, Long buildingTypeId) throws IOException, URISyntaxException {
        List<ResourceRepository.ResourceByVillageResponse> resourcesByVillageId = resourceService.getResourcesByVillageId(villageId);

        // Aggregate Resources
        resourceService.aggregateAndUpdateResources(resourcesByVillageId, villageId);
        resourcesByVillageId = resourceService.getResourcesByVillageId(villageId);

        // UpdateCosts and BuildingTime for next Building Level
        Long nextBuildingLevel = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId).getBuildingLevel() + 1;
        JSONObject updateCosts = getBuildingUpdateDetails(buildingTypeId, nextBuildingLevel, "updatecosts");
        Long updateDuration = getBuildingUpdateDetails(buildingTypeId, nextBuildingLevel, "buildingtime").optLong("seconds");

        ResourceRepository.ResourceByVillageResponse stoneByVillageId = resourcesByVillageId.stream()
                .filter(res -> res.getResourceTypeId().equals(ResourceType.STONE.getValue()) && res.getResourceAtUpdateTime() != null)
                .findFirst().orElseThrow(() -> new IllegalStateException("Stone resource not found"));

        Float stoneAfterLvlIncrease = resourcesAfterLvlIncrease(stoneByVillageId, updateCosts.getLong(ResourceType.STONE.getFullName()));
        ResourceRepository.ResourceByVillageResponse woodByVillageId = resourcesByVillageId.stream()
                .filter(res -> res.getResourceTypeId().equals(ResourceType.WOOD.getValue()) && res.getResourceAtUpdateTime() != null)
                .findFirst().orElseThrow(() -> new IllegalStateException("Wood resource not found"));

        Float woodAfterLvlIncrease = resourcesAfterLvlIncrease(woodByVillageId, updateCosts.getLong(ResourceType.WOOD.getFullName()));

        // ToDo: (HttpResponseBody, welche Resource fehlt)
        if (stoneAfterLvlIncrease < 0 || woodAfterLvlIncrease < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Not enough Resources");
        } else {
            // Decrease Resources by UpdateCosts
            ResourceDto stoneRes = new ResourceDto(ResourceType.STONE.getValue(), stoneAfterLvlIncrease.longValue(), stoneByVillageId.getResourceIncome(), stoneByVillageId.getUpdateTime());
            ResourceDto woodRes = new ResourceDto(ResourceType.WOOD.getValue(), woodAfterLvlIncrease.longValue(), woodByVillageId.getResourceIncome(), woodByVillageId.getUpdateTime());
            ResourceUpdateRequestDTO resourceUpdateRequestDTO = new ResourceUpdateRequestDTO(villageId, List.of(stoneRes, woodRes));
            resourceService.updateResourcesByVillageId(resourceUpdateRequestDTO);

            // Add new line for upcoming resourceIncome
            List<Long> resourceIncomeDependentBuildingTypeIds = List.of(BuildingType.MILL.getValue(), BuildingType.LUMBERJACK.getValue(), BuildingType.MASON.getValue(), BuildingType.IRON_MINE.getValue());
            if (resourceIncomeDependentBuildingTypeIds.contains(buildingTypeId)) {
                JSONObject resourceIncome = getBuildingUpdateDetails(buildingTypeId, nextBuildingLevel, "income");
                Long resourceTypeId = resourceIncome.optLong("resourceTypeId");
                Long resIncome = resourceIncome.optLong("value");
                resourceService.insertResource(villageId, resourceTypeId, null, resIncome);
            }

            // Update Building Level
            var updatedBuilding = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId);
            if (updatedBuilding != null) {
                updatedBuilding.setBuildingLevel(updatedBuilding.getBuildingLevel() + 1);
                updatedBuilding.setUpdateTime(LocalDateTime.now().plusSeconds(updateDuration));
                buildingRepository.save(updatedBuilding);
            }
            return ResponseEntity.ok("Building Increased successfully");
        }
    }

    private JSONObject getBuildingUpdateDetails(Long buildingTypeId, Long buildingLevel, String levelDetail) throws IOException, URISyntaxException {
        String jsonContent = FileReader.readResourceFile("buildings.json");
        JSONObject buildingJsonObject = new JSONObject(jsonContent);
        JSONObject building = buildingJsonObject.getJSONObject(buildingTypeId.toString());
        JSONObject levels = building.getJSONObject("levels");
        JSONObject levelDetails = levels.getJSONObject(buildingLevel.toString());
        return levelDetails.getJSONObject(levelDetail);
    }

    private Float resourcesAfterLvlIncrease(ResourceRepository.ResourceByVillageResponse resObj, Long resRequired) {
        LocalDateTime now = LocalDateTime.now();
        long timeDiffInSecs = Duration.between(resObj.getUpdateTime(), now).getSeconds();
        Float resIncomePerSec = resObj.getResourceIncome().floatValue() / 3600;
        Float resNow = resObj.getResourceAtUpdateTime() + resIncomePerSec * timeDiffInSecs;
        return resNow - resRequired.floatValue();
    }
}