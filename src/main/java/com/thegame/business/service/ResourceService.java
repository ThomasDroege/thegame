package com.thegame.business.service;

import com.thegame.business.dto.ResourceDto;
import com.thegame.business.dto.ResourceUpdateRequestDTO;
import com.thegame.business.enums.ResourceType;
import com.thegame.business.model.Resource;
import com.thegame.business.repository.ResourceRepository;
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

    public List<ResourceRepository.ResourceByVillageResponse> getResourcesByVillageId(Long villageId) {
        return resourceRepository.getResourcesByVillageId(villageId);
    }

    public List<ResourceRepository.ResourceByVillageResponse> getAggregatedResourcesByVillageId(Long villageId) {
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

    public void aggregateAndUpdateResources(List<ResourceRepository.ResourceByVillageResponse> resourcesByVillageId, Long villageId) {
        List<ResourceDto> updateList = new ArrayList<>();

        this.aggregateResource(resourcesByVillageId, villageId, ResourceType.FOOD.getValue(), updateList);
        this.aggregateResource(resourcesByVillageId, villageId, ResourceType.WOOD.getValue(), updateList);
        this.aggregateResource(resourcesByVillageId, villageId, ResourceType.STONE.getValue(), updateList);
        this.aggregateResource(resourcesByVillageId, villageId, ResourceType.IRON.getValue(), updateList);

        if (!updateList.isEmpty()) {
            updateResourcesByVillageId(new ResourceUpdateRequestDTO(villageId, updateList));
        }
    }

    private void aggregateResource(List<ResourceRepository.ResourceByVillageResponse> resourcesByVillageId, Long villageId, Long resourceTypeId, List<ResourceDto> updateList) {
        List<ResourceRepository.ResourceByVillageResponse> resourceObjectByResourceTypeId = new ArrayList<>();

        for (ResourceRepository.ResourceByVillageResponse res : resourcesByVillageId) {
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
                .map(ResourceRepository.ResourceByVillageResponse::getResourceIncome)
                .max(Long::compareTo)
                .orElse(0L);

        if (resourceObjectByResourceTypeId.size() == 2 && resourceObjectByResourceTypeId.get(1).getUpdateTime().isBefore(LocalDateTime.now())) {
            deleteResourceByUpdateTime(villageId, resourceTypeId, resourceObjectByResourceTypeId.get(0).getUpdateTime());
            updateList.add(new ResourceDto(resourceTypeId, aggregatedResources.longValue(), maxIncomeByResourceTypeId, LocalDateTime.now()));
        }
    }
}
