package com.thegame.business.service;

import com.thegame.business.dto.ResourceDto;
import com.thegame.business.dto.ResourceUpdateRequestDTO;
import com.thegame.business.dto.ResponseDto;
import com.thegame.business.enums.ResourceType;
import com.thegame.business.model.BuildingLevel;
import com.thegame.business.model.Resource;
import com.thegame.business.repository.ResourceByVillageResponse;
import com.thegame.business.repository.ResourceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public List<ResponseDto> decreaseRessBeforeBuildAction(Long villageId, BuildingLevel buildingLevel) {
        final List<ResourceByVillageResponse> ressources = resourceRepository.getResourcesByVillageId(villageId);
        final ResourceByVillageResponse stone = ressources.stream().filter(res -> res.getResourceTypeId()
                        .equals(ResourceType.STONE.getValue()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Keine Stein Resource gefunden."));

        final ResourceByVillageResponse wood = ressources.stream().filter(res -> res.getResourceTypeId()
                        .equals(ResourceType.WOOD.getValue()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Keine Holz Resource gefunden."));

        final float stoneNow = getRessourceTotalNow(stone);
        final float stoneAfterLevelIncrease = stoneNow - buildingLevel.getUpdateCostStone().floatValue();

        final float woodNow = getRessourceTotalNow(wood);
        final float woodAfterLevelIncrease = woodNow - buildingLevel.getUpdateCostWood().floatValue();


        if (stoneAfterLevelIncrease >= 0 && woodAfterLevelIncrease >= 0) {
            decreaseResByUpdateCosts(villageId,
                    (long) stoneAfterLevelIncrease, stone.getResourceIncome(), LocalDateTime.now(),
                    (long) woodAfterLevelIncrease, wood.getResourceIncome(), LocalDateTime.now());

        }

        return checkForMissingResources(stoneAfterLevelIncrease, woodAfterLevelIncrease);

    }

    /**
     * Res_Total = Res_At_UpdateTime + Modifier * [
     *   Diff(IncomeTime-TotalTime) * income_Alt
     * + Diff(IncomeTime-Now)  * income_Neu
     * ]
     * Bisher ohne Modifier betrachtet
     * @param resource
     * @return
     */
    public float getRessourceTotalNow(ResourceByVillageResponse resource) {
        final LocalDateTime now = LocalDateTime.now();
        final float resIncomeModifier = 1;

        //ToDo: aktualisieren: d.h. Bezug der Infos aus BuildingLevelRepository [Welches Lvl hat das Gebäude in dem Village]
        final Long incomeAlt = 20L;
        final float incomeAltPerSec = incomeAlt.floatValue() / 3600;
        final Long incomeNeu = 30L;
        final float incomeNeuPerSec = incomeNeu.floatValue() / 3600;

        long diffIncomeUpdateTime = Duration.between(resource.getResourceIncomeUpdateTime(), resource.getUpdateTime()).getSeconds();
        long diffIncomeNowTime = Duration.between(resource.getResourceIncomeUpdateTime(), now).getSeconds();

        // ToDo: conditional Hinzufügen der Komponenten, je nachdem, ob Diff pos. oder neg.
        final float ressTotal = resource.getResourceAtUpdateTime()
                + resIncomeModifier * (diffIncomeUpdateTime * incomeAltPerSec + diffIncomeNowTime * incomeNeuPerSec);
        return ressTotal;

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


    //ToDo: evtl private
    public List<ResourceByVillageResponse> getResourcesByVillageId(Long villageId) {
        return resourceRepository.getResourcesByVillageId(villageId);
    }

    public List<ResourceByVillageResponse> getAggregatedResourcesByVillageId(Long villageId) {
        return resourceRepository.getAggregatedResourcesByVillageId(villageId);
    }

    public void updateResourcesByVillageId(ResourceUpdateRequestDTO request) {
        for (ResourceDto resource : request.getResources()) {
            resourceRepository.updateResourcesByVillageId(
                    resource.getResourceTypeId(),
                    request.getVillageId(),
                    resource.getResourceAtUpdateTime(),
                    resource.getResourceIncome(),
                    resource.getUpdateTime()
            );
        }
    }

    public void deleteResourceByUpdateTime(Long villageId, Long resourceTypeId, LocalDateTime updateTime) {
        resourceRepository.deleteResourceByUpdateTime(villageId, resourceTypeId, updateTime);
    }

    public void insertResource(Long villageId, Long resourceTypeId, Long resourceAtUpdateTime, Long resourceIncome) {
        resourceRepository.insertResourceByVillageId(villageId, resourceTypeId, resourceAtUpdateTime, resourceIncome);
    }

    public void aggregateAndUpdateResources(List<ResourceByVillageResponse> resourcesByVillageId, Long villageId) {
        List<ResourceDto> updateList = new ArrayList<>();

        this.aggregateResource(resourcesByVillageId, villageId, ResourceType.FOOD.getValue(), updateList);
        this.aggregateResource(resourcesByVillageId, villageId, ResourceType.WOOD.getValue(), updateList);
        this.aggregateResource(resourcesByVillageId, villageId, ResourceType.STONE.getValue(), updateList);
        this.aggregateResource(resourcesByVillageId, villageId, ResourceType.IRON.getValue(), updateList);

        if (!updateList.isEmpty()) {
            updateResourcesByVillageId(new ResourceUpdateRequestDTO(villageId, updateList));
        }
    }

    private void aggregateResource(List<ResourceByVillageResponse> resourcesByVillageId, Long villageId, Long resourceTypeId, List<ResourceDto> updateList) {
        List<ResourceByVillageResponse> resourceObjectByResourceTypeId = new ArrayList<>();

        for (ResourceByVillageResponse res : resourcesByVillageId) {
            if (res.getResourceTypeId().equals(resourceTypeId)) {
                resourceObjectByResourceTypeId.add(res);
            }
        }

        resourceObjectByResourceTypeId.sort((res1, res2) -> res1.getUpdateTime().compareTo(res2.getUpdateTime()));

        if (resourceObjectByResourceTypeId.isEmpty()) {
            return;
        }

        Double aggregatedResources = 0.0;
        if (resourceObjectByResourceTypeId.get(0).getResourceAtUpdateTime() != null) {
            aggregatedResources = resourceObjectByResourceTypeId.get(0).getResourceAtUpdateTime()
                    + Duration.between(resourceObjectByResourceTypeId.get(0).getUpdateTime(), LocalDateTime.now()).getSeconds()
                    * resourceObjectByResourceTypeId.get(0).getResourceIncome() / 3600.0;
        }

        Long maxIncomeByResourceTypeId = resourceObjectByResourceTypeId.stream()
                .map(ResourceByVillageResponse::getResourceIncome)
                .max(Long::compareTo)
                .orElse(0L);

        if (resourceObjectByResourceTypeId.size() == 2 && resourceObjectByResourceTypeId.get(1).getUpdateTime().isBefore(LocalDateTime.now())) {
            deleteResourceByUpdateTime(villageId, resourceTypeId, resourceObjectByResourceTypeId.get(0).getUpdateTime());
            updateList.add(new ResourceDto(resourceTypeId, aggregatedResources.longValue(), maxIncomeByResourceTypeId, LocalDateTime.now()));
        }
    }

    //ToDo: anpassen auf neue Datenbankstruktur
    void decreaseResByUpdateCosts(Long villageId, Long stoneAfterLvlIncrease, Long stoneIncome, LocalDateTime stoneUpdateTime,
                                  Long woodAfterLvlIncrease, Long woodIncome, LocalDateTime woodResUpdateTime) {
        ResourceDto stoneRes = new ResourceDto(ResourceType.STONE.getValue(), stoneAfterLvlIncrease, stoneIncome, stoneUpdateTime);
        ResourceDto woodRes = new ResourceDto(ResourceType.WOOD.getValue(), woodAfterLvlIncrease, woodIncome, woodResUpdateTime);
        ResourceUpdateRequestDTO resourceUpdateRequestDTO = new ResourceUpdateRequestDTO(villageId, List.of(stoneRes, woodRes));
        updateResourcesByVillageId(resourceUpdateRequestDTO);
    }
}
