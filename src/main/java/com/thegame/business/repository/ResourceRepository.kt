package com.thegame.business.repository

import com.thegame.business.dto.ResourceUpdateRequestDTO
import com.thegame.business.model.Resource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDateTime

@Repository
interface ResourceRepository: JpaRepository<Resource, Long> {

    @Query(nativeQuery = true, value = STMT_RESOURCES_BY_VILLAGE_ID)
    fun getResourcesByVillageId(villageId: Long):List<ResourceByVillageResponse>

    @Modifying // For UPDATE, DELETE or INSERT statements in Spring Data JPA needed
    @Transactional // For UPDATE, DELETE or INSERT statements in JPA needed
    @Query(nativeQuery = true, value = STMT_UPDATE_RESOURCES_BY_VILLAGE_ID)
    fun updateResourcesByVillageId(
        @Param("resourceTypeId") resourceTypeId: Long,
        @Param("villageId") villageId: Long,
        @Param("resourceAtUpdateTime") resourceAtUpdateTime: Long,
        @Param("resourceIncome") resourceIncome: Long
        )

    interface ResourceByVillageResponse {
        val resourceTypeId: Long
        val resourceName: String
        val resourceAtUpdateTime: Long
        val resourceIncome: Long
        val updateTime: LocalDateTime
    }

    companion object {
        private const val   STMT_RESOURCES_BY_VILLAGE_ID = """
        SELECT  r.resource_type_id as resourceTypeId,
                r.resource_at_update_time as resourceAtUpdateTime,
                r.resource_income as resourceIncome,
                r.update_time as updateTime,
                rt.resource_name as resourceName
        FROM data.resources r
        JOIN data.resource_types rt ON rt.resource_type_id = r.resource_type_id 
        WHERE  village_id = :villageId"""

        private const val STMT_UPDATE_RESOURCES_BY_VILLAGE_ID = """
        UPDATE data.resources
        SET resource_at_update_time = :resourceAtUpdateTime,
            resource_income = :resourceIncome,
            update_time = CURRENT_TIMESTAMP
        WHERE resource_type_id = :resourceTypeId 
        AND village_id = :villageId"""
    }
}