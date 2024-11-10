package com.thegame.business.service

import com.thegame.business.dto.ResourceDto
import com.thegame.business.dto.ResourceUpdateRequestDTO
import com.thegame.business.enums.BuildingType
import com.thegame.business.enums.ResourceType
import com.thegame.business.repository.BuildingRepository
import com.thegame.business.repository.ResourceRepository
import com.thegame.business.utils.FileReader
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class BuildingService(private val buildingRepository: BuildingRepository,
                      private val resourceService: ResourceService) {

    fun getBuildingsByVillageId(villageId: Long): List<BuildingRepository.BuildingsByVillageIdResponse> {
        return buildingRepository.getBuildingsByVillageId(villageId)
    }

    /**
     * Startet die Erhöhung des Gebäude-Levels.
     *
     * Diese Methode zieht die Resourcen für das Gebäude Update ab und setzt das Gebäude Level hoch.
     * Die UpdateTime wird entsprechend der Bauzeit in die Zukunft gesetzt.
     */
    fun buildingUpgrade(villageId: Long, buildingTypeId: Long): ResponseEntity<String> {

        //UpdateCosts and BuildingTime for next Building Lvl
        val nextBuildingsLvl = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId)!!.buildingLevel!!.toLong() + 1
        val updateCosts = getBuildingUpdateDetails(buildingTypeId, nextBuildingsLvl, "updatecosts")
        val updateDuration = getBuildingUpdateDetails(buildingTypeId, nextBuildingsLvl, "buildingtime")?.getLong("seconds")

        //TODO: ggf. noch anpassen, weil mehrere Zeilen zusammengerechnet werden müssen
        var resourcesByVillageId = resourceService.getResourcesByVillageId(villageId)

        val stoneByVillageId = resourcesByVillageId.first { res -> res.resourceTypeId == ResourceType.STONE.value && res.resourceAtUpdateTime != null }
        val woodByVillageId = resourcesByVillageId.first { res -> res.resourceTypeId == ResourceType.WOOD.value && res.resourceAtUpdateTime != null  }

        //TODO: was passiert, wenn resource_at_update_time = null?
        val stoneAfterLvlIncrease = resourcesAfterLvlIncrease(stoneByVillageId, updateCosts!!.getLong(ResourceType.STONE.fullName))
        val woodAfterLvlIncrease = resourcesAfterLvlIncrease(woodByVillageId, updateCosts.getLong(ResourceType.WOOD.fullName))

        //ToDo: (HttpResponseBody, welche Resource fehlt)
        if(stoneAfterLvlIncrease < 0 || woodAfterLvlIncrease < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Not enough Resources")
        } else {



/*
            // Decrease Resources by UpdateCosts
            val stoneRes = ResourceDto(ResourceType.STONE.value, stoneAfterLvlIncrease.toLong(), stoneByVillageId.resourceIncome, stoneByVillageId.updateTime)
            val woodRes = ResourceDto(ResourceType.WOOD.value, woodAfterLvlIncrease.toLong(), woodByVillageId.resourceIncome, woodByVillageId.updateTime)
            val resourceUpdateRequestDTO =  ResourceUpdateRequestDTO(villageId, listOf(stoneRes, woodRes))
            resourceService.updateResourcesByVillageId(resourceUpdateRequestDTO)
*/


            // Aggregate Resources
            resourceService.aggregateAndUpdateResources(resourcesByVillageId, villageId)


            // Add new line for upcoming resourceIncome
            //ToDo: Bauen der Liste in BuildingType Enum auslagern
            val resourceIncomeDependentBuildingTypeIds = listOf(BuildingType.MILL.value, BuildingType.LUMBERJACK.value, BuildingType.MASON.value, BuildingType.IRON_MINE.value)
            if(resourceIncomeDependentBuildingTypeIds.contains(buildingTypeId)) {
                val resourceIncome = getBuildingUpdateDetails(buildingTypeId, nextBuildingsLvl, "income")
                val resourceTypeId = resourceIncome!!.getLong("resourceTypeId")
                val resIncome = resourceIncome.getLong("value")
                resourceService.insertResource(villageId, resourceTypeId , null, resIncome)
            }



            // Update Building Level
            val updatedBuilding = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId)
            if(updatedBuilding != null) {
                updatedBuilding.buildingLevel = updatedBuilding.buildingLevel?.plus(1)
                updatedBuilding.updateTime = LocalDateTime.now().plusSeconds(updateDuration!!)
                buildingRepository.save(updatedBuilding)
            }

            return ResponseEntity.ok("Building Increased successfully")
        }
    }

    private fun getBuildingUpdateDetails(buildingTypeId: Long, buildingLevel: Long, levelDetail: String): JSONObject? {
        val jsonContent = FileReader.readResourceFile("buildings.json")
        val buildingJsonObject = JSONObject(jsonContent)
        val building = buildingJsonObject.getJSONObject(buildingTypeId.toString())
        val levels = building.getJSONObject("levels")
        val levelDetails = levels.getJSONObject(buildingLevel.toString())
        return levelDetails.getJSONObject(levelDetail)
    }


    private fun resourcesAfterLvlIncrease(resObj: ResourceRepository.ResourceByVillageResponse, resRequired: Long): Float {
        val now = LocalDateTime.now()
        val timeDiffInSecs = Duration.between(resObj.updateTime, now).seconds
        val resIncomePerSec = resObj.resourceIncome.toFloat()/3600
        val resNow = resObj.resourceAtUpdateTime?.plus(resIncomePerSec*timeDiffInSecs)

        return resNow!!.minus(resRequired.toFloat())
    }
}
