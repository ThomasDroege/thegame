package com.thegame.business.repository

import com.thegame.business.model.Resource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TimerRepository: JpaRepository<Resource, Long> {

    @Query(nativeQuery = true, value = STMT_BUILDING_TIMER_BY_VILLAGE_ID)
    fun getBuildingTimerByVillageId(villageId: Long):List<BuildingTimerByVillageResponse>

    interface BuildingTimerByVillageResponse {
        val timerType: Long
        val objectTypeId: Long
        val updateTime: LocalDateTime
    }

    companion object {
        private const val   STMT_BUILDING_TIMER_BY_VILLAGE_ID = """
        SELECT  t.timer_type_id as timerType,
                t.object_type_id as objectTypeId,
                t.update_time as updateTime
        FROM data.timer t
        WHERE  village_id = :villageId
        AND timer_type_id = 1"""
    }
}