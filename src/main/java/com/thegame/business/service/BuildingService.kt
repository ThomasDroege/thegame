package com.thegame.business.service

import com.thegame.business.repository.BuildingRepository
import org.springframework.stereotype.Service

@Service
class BuildingService(private val buildingRepository: BuildingRepository) {

    fun getBuildingsByVillageId(villageId: Long): List<BuildingRepository.BuildingByVillageResponse> {
        return buildingRepository.getBuildingsByVillageId(villageId)
    }
}