package com.thegame.business.service

import com.thegame.business.dto.ResourceDto
import com.thegame.business.dto.ResourceUpdateRequestDTO
import com.thegame.business.enums.ResourceType
import com.thegame.business.repository.BuildingRepository
import com.thegame.business.repository.ResourceRepository
import com.thegame.business.utils.FileReader
import org.json.JSONException
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

    //ToDo: (HttpResponseBody, welche Resource fehlt)
    /**
     * Startet die Erhöhung des Gebäude-Levels.
     *
     * Diese Methode zieht die Resourcen für das Gebäude Update ab.
     * Im Frontend wird ein Timer gestartet und nach Ablaufen dieses wird über eine separate Route
     * das Gebäude Level hochgesetzt und die Verbesserung des Gebäudes geupdated (wie z.B. Ertragserhöhung)
     */
    fun initiateBuildingUpgrade(villageId: Long, buildingTypeId: Long): ResponseEntity<String> {
        val resourcesByVillageId = resourceService.getResourcesByVillageId(villageId)
        val stoneByVillageId = resourcesByVillageId.first { res -> res.resourceTypeId == ResourceType.STONE.value }
        val woodByVillageId = resourcesByVillageId.first { res -> res.resourceTypeId == ResourceType.WOOD.value }

        val nextBuildingsLvl = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId)!!.buildingLevel!!.toLong() + 1
        val updateCosts = getBuildingUpdateDetails(buildingTypeId, nextBuildingsLvl, "updatecosts")

        val stoneAfterLvlIncrease = resourcesAfterLvlIncrease(stoneByVillageId, updateCosts!!.getLong(ResourceType.STONE.fullName))
        val woodAfterLvlIncrease = resourcesAfterLvlIncrease(woodByVillageId, updateCosts.getLong(ResourceType.WOOD.fullName))
        if(stoneAfterLvlIncrease >= 0 && woodAfterLvlIncrease >= 0){
            val stoneRes = ResourceDto(ResourceType.STONE.value, stoneAfterLvlIncrease.toLong(), stoneByVillageId.resourceIncome)
            val woodRes = ResourceDto(ResourceType.WOOD.value, woodAfterLvlIncrease.toLong(), woodByVillageId.resourceIncome)
            val resourceUpdateRequestDTO =  ResourceUpdateRequestDTO(villageId, listOf(stoneRes, woodRes))
            resourceService.updateResourcesByVillageId(resourceUpdateRequestDTO)
            return ResponseEntity.ok("Building Increased successfully")
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Not enough Resources")
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
        val resNow= resObj.resourceAtUpdateTime + resIncomePerSec*timeDiffInSecs

        return resNow - resRequired.toFloat()
    }

    fun increaseBuildingLevel(villageId: Long, buildingTypeId: Long) {
        val updatedBuilding = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId)
        if(updatedBuilding != null) {
            updatedBuilding.buildingLevel = updatedBuilding.buildingLevel?.plus(1)
            updatedBuilding.updateTime = LocalDateTime.now()
            buildingRepository.save(updatedBuilding)

            try{
                val nextBuildingsLvl = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId)!!.buildingLevel!!.toLong()
                val resourceIncome = getBuildingUpdateDetails(buildingTypeId, nextBuildingsLvl, "income")
                val resourceTypeId = resourceIncome!!.getLong("resourceTypeId")
                val resIncome = resourceIncome.getLong("value")
                val resourcesByVillageId = resourceService.getResourcesByVillageId(villageId)
                val resourceByVillageId = resourcesByVillageId.first { res ->  res.resourceTypeId ==  resourceTypeId }
                val updateResource = ResourceDto(resourceTypeId, resourceByVillageId.resourceAtUpdateTime, resIncome)
                val resourceUpdateRequestDTO =  ResourceUpdateRequestDTO(villageId, listOf(updateResource))
                resourceService.updateResourcesByVillageId(resourceUpdateRequestDTO)

            } catch (e: JSONException) {
                println("Info: Update of building without increase of resource outcome.")
            }
        } else {
            //TODO: Http Code zurückgeben
            print("TODO: HTTP Code zurückgeben")
        }
    }
}
