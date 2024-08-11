package com.thegame.business.service

import com.thegame.business.dto.ResourceUpdateRequestDTO
import com.thegame.business.model.Resource
import com.thegame.business.repository.ResourceRepository
import org.springframework.stereotype.Service

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
}