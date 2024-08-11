package com.thegame.business.repository

import com.thegame.business.model.Building
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface BuildingRepository: JpaRepository<Building, Long> {

    @Query(nativeQuery = true, value = STMT_BUILDINGS_BY_VILLAGE_ID)
    fun getBuildingsByVillageId(villageId: Long):List<BuildingsByVillageIdResponse>

    @Query(nativeQuery = true, value = STMT_BUILDING_BY_VILLAGE_ID_AND_BUILDING_ID)
    fun getBuildingByVillageIdAndBuildingId(villageId: Long, buildingTypeId: Long):Building?

    interface BuildingsByVillageIdResponse {
        val buildingId: Long
        val buildingTypeId: Long
        val buildingName: String
        val buildingLevel: Long
        val updateTime: LocalDateTime
    }

    companion object {
        private const val   STMT_BUILDINGS_BY_VILLAGE_ID = """
        SELECT  b.building_id as buildingId,
                b.building_type_id as buildingTypeId,
                bt.building_name as buildingName,
                b.building_level as buildingLevel,
                b.update_time as updateTime
        FROM data.buildings b
        JOIN data.building_types bt ON bt.building_type_id = b.building_type_id 
        WHERE  village_id = :villageId"""

        private const val   STMT_BUILDING_BY_VILLAGE_ID_AND_BUILDING_ID = """
        SELECT building_id,
               village_id,
               building_type_id,
               building_level,
               update_time
        FROM data.buildings b
        WHERE village_id = :villageId
        AND building_type_id = :buildingTypeId"""
    }
}