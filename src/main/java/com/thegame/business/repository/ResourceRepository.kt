package com.thegame.business.repository

import com.thegame.business.model.Resource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ResourceRepository: JpaRepository<Resource, Long> {

    @Query(nativeQuery = true, value = STMT_RESOURCES_BY_VILLAGE_ID)
    fun getResourcesByVillageId(villageId: Long):List<ResourceByVillageResponse>

    interface ResourceByVillageResponse {
        val resourceId: Long
        val resourceName: String
        val resourceAtUpdateTime: Long
        val resourceIncome: Long
        val updateTime: String
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
    }
}