package com.thegame.business.repository

import com.thegame.business.model.Resource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
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
    fun updateResourcesByVillageId()

    interface ResourceByVillageResponse {
        val resourceId: Long
        val resourceName: String
        val resourceAtUpdateTime: Long
        val resourceIncome: Long
        val updateTime: LocalDateTime
    }

    companion object {
        private const val   STMT_RESOURCES_BY_VILLAGE_ID = """
        SELECT  r.resource_id as resourceId,
                r.resource_at_update_time as resourceAtUpdateTime,
                r.resource_income as resourceIncome,
                r.update_time as updateTime,
                rt.resource_name as resourceName
        FROM data.resources r
        JOIN data.resource_types rt ON rt.resource_type_id = r.resource_type_id 
        WHERE  village_id = :villageId"""

        private const val STMT_UPDATE_RESOURCES_BY_VILLAGE_ID = """
        UPDATE data.resources 
        SET resource_at_update_time = CASE 
            WHEN resource_type_id = 3 THEN 1000 
            WHEN resource_type_id = 4 THEN 2000 
        END,
        resource_income = CASE 
            WHEN resource_type_id = 3 THEN 66 
            WHEN resource_type_id = 4 THEN 77 
        END,
        update_time = now()
        WHERE village_id = 1 AND resource_type_id IN (3, 4)   
        """
    }
}