package com.thegame.business.service

import com.thegame.business.dto.ResourceDto
import com.thegame.business.dto.ResourceUpdateRequestDTO
import com.thegame.business.enums.ResourceType
import com.thegame.business.model.Resource
import com.thegame.business.repository.ResourceRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class ResourceService(private val resourceRepository: ResourceRepository) {

    fun getAllResources(): List<Resource> {
        return resourceRepository.findAll()
    }

    fun getResourcesByVillageId(villageId: Long): List<ResourceRepository.ResourceByVillageResponse> {
        return resourceRepository.getResourcesByVillageId(villageId)
    }

    fun updateResourcesByVillageId(request: ResourceUpdateRequestDTO) {
        request.resources.forEach { resource ->
            resourceRepository.updateResourcesByVillageId(
                resource.resourceTypeId,
                request.villageId,
                resource.resourceAtUpdateTime,
                resource.resourceIncome
            )
        }
    }

    fun deleteResourceByUpdateTime(villageId: Long, resourceTypeId: Long, updateTime: LocalDateTime) {
        resourceRepository.deleteResourceByUpdateTime(villageId, resourceTypeId, updateTime)
    }

    fun aggregateAndUpdateResources(resourcesByVillageId :List<ResourceRepository.ResourceByVillageResponse>, villageId: Long) {
        val foodTotal = resourcesByVillageId.filter { res -> res.resourceTypeId == ResourceType.FOOD.value }.sortedBy { res -> res.updateTime }
        val woodTotal = resourcesByVillageId.filter { res -> res.resourceTypeId == ResourceType.WOOD.value }.sortedBy { res -> res.updateTime }
        val stoneTotal = resourcesByVillageId.filter { res -> res.resourceTypeId == ResourceType.STONE.value }.sortedBy { res -> res.updateTime }
        val ironTotal = resourcesByVillageId.filter { res -> res.resourceTypeId == ResourceType.IRON.value }.sortedBy { res -> res.updateTime }
        val aggregatedFoodResources = foodTotal[0].resourceAtUpdateTime?.plus(Duration.between(foodTotal[0].updateTime, LocalDateTime.now()).seconds*foodTotal[0].resourceIncome/3600)
        val aggregatedWoodResources = woodTotal[0].resourceAtUpdateTime?.plus(Duration.between(woodTotal[0].updateTime, LocalDateTime.now()).seconds*woodTotal[0].resourceIncome/3600)
        val aggregatedStoneResources = stoneTotal[0].resourceAtUpdateTime?.plus(Duration.between(stoneTotal[0].updateTime, LocalDateTime.now()).seconds*stoneTotal[0].resourceIncome/3600)
        val aggregatedIronResources = ironTotal[0].resourceAtUpdateTime?.plus(Duration.between(ironTotal[0].updateTime, LocalDateTime.now()).seconds*ironTotal[0].resourceIncome/3600)

        val foodIncomeByVillage = foodTotal.maxBy{ res -> res.resourceIncome}.resourceIncome
        val woodIncomeByVillage = woodTotal.maxBy{ res -> res.resourceIncome}.resourceIncome
        val stoneIncomeByVillage = stoneTotal.maxBy{ res -> res.resourceIncome}.resourceIncome
        val ironIncomeByVillage = ironTotal.maxBy{ res -> res.resourceIncome}.resourceIncome

        val updateList: MutableList<ResourceDto> = mutableListOf()
        if(foodTotal.size == 2 && foodTotal[1].updateTime < LocalDateTime.now()) {
            deleteResourceByUpdateTime(villageId, ResourceType.FOOD.value, foodTotal[0].updateTime)
            updateList.add(ResourceDto(ResourceType.FOOD.value, aggregatedFoodResources, foodIncomeByVillage))

        }
        if(woodTotal.size == 2 && woodTotal[1].updateTime < LocalDateTime.now()) {
            deleteResourceByUpdateTime(villageId, ResourceType.WOOD.value, woodTotal[0].updateTime)
            updateList.add(ResourceDto(ResourceType.WOOD.value, aggregatedWoodResources, woodIncomeByVillage))
        }
        if(stoneTotal.size == 2 && stoneTotal[1].updateTime < LocalDateTime.now()) {
            deleteResourceByUpdateTime(villageId, ResourceType.STONE.value, stoneTotal[0].updateTime)
            updateList.add(ResourceDto(ResourceType.STONE.value, aggregatedStoneResources, stoneIncomeByVillage))
        }
        if(ironTotal.size == 2 && ironTotal[1].updateTime < LocalDateTime.now()) {
            deleteResourceByUpdateTime(villageId, ResourceType.IRON.value, ironTotal[0].updateTime)
            updateList.add(ResourceDto(ResourceType.IRON.value, aggregatedIronResources, ironIncomeByVillage))
        }

        if(updateList.isNotEmpty()) {
            updateResourcesByVillageId(ResourceUpdateRequestDTO(villageId, updateList))
        }

    }
}