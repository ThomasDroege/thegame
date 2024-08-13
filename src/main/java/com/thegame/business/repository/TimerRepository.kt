package com.thegame.business.repository

import com.thegame.business.model.Resource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
interface TimerRepository: JpaRepository<Resource, Long> {

    @Query(nativeQuery = true, value = STMT_BUILDING_TIMER_BY_VILLAGE_ID)
    fun getBuildingTimerByVillageId(villageId: Long):List<BuildingTimerByVillageResponse>

    @Modifying // For UPDATE, DELETE or INSERT statements in Spring Data JPA needed
    @Transactional // For UPDATE, DELETE or INSERT statements in JPA needed
    @Query(nativeQuery = true, value = STMT_UPDATE_BUILDING_TIMER_BY_VILLAGE_ID)
    fun updateBuildingTimerByVillageId(
        @Param("villageId") villageId: Long,
        @Param("timerTypeId") timerTypeId: Long,
        @Param("objectTypeId") objectTypeId: Long,
        @Param("updateTime") updateTime: LocalDateTime
    )

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

        private const val STMT_UPDATE_BUILDING_TIMER_BY_VILLAGE_ID = """
        UPDATE data.timer
        SET update_time = :updateTime,
            object_type_id = :objectTypeId
        WHERE timer_type_id = :timerTypeId 
        AND village_id = :villageId"""
    }
}