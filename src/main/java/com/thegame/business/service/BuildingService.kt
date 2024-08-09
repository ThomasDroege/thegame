package com.thegame.business.service

import com.thegame.business.enums.ResourceType
import com.thegame.business.repository.BuildingRepository
import com.thegame.business.repository.ResourceRepository
import com.thegame.business.utils.FileReader
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class BuildingService(private val buildingRepository: BuildingRepository,
                      private val resourceRepository: ResourceRepository) {

    fun getBuildingsByVillageId(villageId: Long): List<BuildingRepository.BuildingsByVillageIdResponse> {
        return buildingRepository.getBuildingsByVillageId(villageId)
    }

    //ToDo: Level mitgeben lassen
    //ToDo: HttpStatus 200 wenn erfolgreich, 409 (Conflict), wenn Ressourcen nicht ausreichen (HttpResponseBody, welche Resource fehlt)
    fun updateBuilding(villageId: Long, buildingTypeId: Long) {
        //ToDo: Kommentar entfernen
        /*
         Button drücken zum Erhöhen des Gebäude Levels
         Gebäude Level wurde mitgegeben und in der env Datei im Backend geschaut, wieviel Resourcen notwendig sind
         Resourcen des Dorfes aus Resource-Table beziehen und checken ob es reicht zum Update
         Falls Ja ein 200 zurückgeben, Falls nein einen anderen 409 (CONFLICT)
         Wenn 200er, dann wird ein Timer im Frontend gestartet, welcher anzeigt, wielange es benötigt, bis Gebäude gebaut ist.
         Es wird ein Update auf die ResourcenTabelle gefahren: Resourcen Einkommen auf ResourceTotal draufrechnen seit letztem Update und danach Resourcen abziehen für den bau
         Wenn Timer abgelaufen, dann wird ein Request zum Update der Resourcen gefahren (Resourcen Total seit letztem Ress Update sowie Einkommen)
         Update Tabelle für Ress Gebäude
      */
        val resourcesByVillageId = resourceRepository.getResourcesByVillageId(villageId)

        //ToDo: Einlesen Auslagern in separate Funktion
        val jsonContent = FileReader.readResourceFile("buildings.json")
        val buildingJsonObject = JSONObject(jsonContent)
        val building = buildingJsonObject.getJSONObject(buildingTypeId.toString())
        //ToDo: diesen fixen Wert weg
        val level = "2"
        val levels = building.getJSONObject("levels")
        val levelDetails = levels.getJSONObject(level)
        val updateCosts = levelDetails.getJSONObject("updatecosts")


        val stoneRequired = updateCosts.getLong("stone")
        val woodRequired = updateCosts.getLong("wood")
        val stoneByVillageId = resourcesByVillageId
                .first { res -> res.resourceId.equals(ResourceType.STONE.value) }
        val woodByVillageId = resourcesByVillageId
            .first { res -> res.resourceId.equals(ResourceType.WOOD.value) }
        val stoneEnough = isEnoughResourcesAvailable(stoneByVillageId, stoneRequired)
        val woodEnough = isEnoughResourcesAvailable(woodByVillageId, woodRequired)
        if(stoneEnough && woodEnough){
            //ToDo: ResourcenUpdateRoute fahren (Abziehen der Resourcen)
            //ToDo: increaseLevel später raus, wird nach Ablaufen des Timers aufgerufen
            //ToDo: danach wird ResourceTabelle zum IncomeErhöhen geupdated
            increaseBuildingLevel(villageId, buildingTypeId)
        }
    }

    private fun isEnoughResourcesAvailable(resObj: ResourceRepository.ResourceByVillageResponse, resRequired: Long): Boolean {
        val now = LocalDateTime.now()
        val timeDiffInSecs = Duration.between(resObj.updateTime, now).seconds
        val resIncomePerSec = resObj.resourceIncome.toFloat()/3600
        val resNow= resObj.resourceAtUpdateTime + resIncomePerSec*timeDiffInSecs

        return if(resRequired.toFloat() < resNow) {
            true
        } else {
            false
        }
    }

    private fun increaseBuildingLevel(villageId: Long, buildingTypeId: Long) {
        val updatedBuilding = buildingRepository.getBuildingByVillageIdAndBuildingId(villageId, buildingTypeId)
        if(updatedBuilding != null) {
            updatedBuilding.buildingLevel = updatedBuilding.buildingLevel?.plus(1)
            //ToDO: evtl wird UpdateTime nicht mehr benötigt, da ja das Gebäude nach Ablaufen des Timers geupdated wird. Jedoch ist updatetime immer eine "interessante" Größe
            updatedBuilding.updateTime = LocalDateTime.now()
            buildingRepository.save(updatedBuilding)
        } else {
            print("TODO: HTTP Code zurückgeben")
        }
    }
}
