package com.thegame.business.service

import com.thegame.business.repository.BuildingRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BuildingService(private val buildingRepository: BuildingRepository) {

    fun getBuildingsByVillageId(villageId: Long): List<BuildingRepository.BuildingsByVillageIdResponse> {
        return buildingRepository.getBuildingsByVillageId(villageId)
    }

    fun increaseBuildingLevel(villageId: Long, buildingTypeId: Long) {
        val updatedBuilding = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId)
        if(updatedBuilding != null) {
            updatedBuilding.buildingLevel = updatedBuilding.buildingLevel?.plus(1)
            //TODO: hier muss noch die UpdateTime = LocalDateTime.now() + UpdateZeit hinzugefügt werden
            // An anderer Stelle muss noch in der UI das Level - 1 angezeigt werden, wenn LocalDateTime.now() < updateTim ist
            updatedBuilding.updateTime = LocalDateTime.now()
            buildingRepository.save(updatedBuilding)
        } else {
            print("TODO: HTTP Code zurückgeben")
        }
    }
}