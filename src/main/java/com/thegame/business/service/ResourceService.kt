package com.thegame.business.service

import com.thegame.business.dto.ResourceDto
import com.thegame.business.dto.ResourceUpdateRequestDTO
import com.thegame.business.enums.ResourceType
import com.thegame.business.model.Resource
import com.thegame.business.repository.ResourceRepository
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ResourceService(private val resourceRepository: ResourceRepository) {

    fun getAllResources(): List<Resource> {
        return resourceRepository.findAll()
    }

    fun getResourcesByVillageId(villageId: Long): List<ResourceRepository.ResourceByVillageResponse> {
        return resourceRepository.getResourcesByVillageId(villageId)
    }

    fun getAggregatedResourcesByVillageId(villageId: Long): List<ResourceRepository.ResourceByVillageResponse> {
        return resourceRepository.getAggregatedResourcesByVillageId(villageId)
    }

    fun updateResourcesByVillageId(request: ResourceUpdateRequestDTO) {
        request.resources.forEach { resource ->
            resourceRepository.updateResourcesByVillageId(
                resource.resourceTypeId,
                request.villageId,
                resource.resourceAtUpdateTime,
                resource.resourceIncome,
                resource.updateTime
            )
        }
    }

    fun deleteResourceByUpdateTime(villageId: Long, resourceTypeId: Long, updateTime: LocalDateTime) {
        resourceRepository.deleteResourceByUpdateTime(villageId, resourceTypeId, updateTime)
    }

    fun insertResource(villageId: Long, resourceTypeId: Long, resourceAtUpdateTime: Long?, resourceIncome: Long) {
        resourceRepository.insertResourceByVillageId(villageId, resourceTypeId, resourceAtUpdateTime, resourceIncome)
    }



    fun aggregateAndUpdateResources(resourcesByVillageId :List<ResourceRepository.ResourceByVillageResponse>, villageId: Long) {
        val updateList: MutableList<ResourceDto> = mutableListOf()

        fun aggregateResource(resourceTypeId: Long) {
            val resourceObjectByResourceTypeId = resourcesByVillageId.filter { res -> res.resourceTypeId == resourceTypeId }.sortedBy { res -> res.updateTime }
            val aggregatedResources = resourceObjectByResourceTypeId[0].resourceAtUpdateTime?.plus(Duration.between(resourceObjectByResourceTypeId[0].updateTime, LocalDateTime.now()).seconds*resourceObjectByResourceTypeId[0].resourceIncome/3600)
            val maxIncomeByResourceTypeId = resourceObjectByResourceTypeId.maxBy{ res -> res.resourceIncome}.resourceIncome
            if(resourceObjectByResourceTypeId.size == 2 && resourceObjectByResourceTypeId[1].updateTime < LocalDateTime.now()) {
                deleteResourceByUpdateTime(villageId, resourceTypeId, resourceObjectByResourceTypeId[0].updateTime)
                updateList.add(ResourceDto(resourceTypeId, aggregatedResources, maxIncomeByResourceTypeId, LocalDateTime.now()))
            }
        }

        aggregateResource(ResourceType.FOOD.value)
        aggregateResource(ResourceType.WOOD.value)
        aggregateResource(ResourceType.STONE.value)
        aggregateResource(ResourceType.IRON.value)

        if(updateList.isNotEmpty()) {
            updateResourcesByVillageId(ResourceUpdateRequestDTO(villageId, updateList))
        }

    }
}