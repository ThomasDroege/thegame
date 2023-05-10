package com.thegame.business.repository

import com.thegame.business.model.Resource
import jakarta.persistence.NamedNativeQuery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ResourceRepository: JpaRepository<Resource, Long> {


    @Query(nativeQuery = true, value = STMT_RESOURCES_BY_VILLAGE_ID)
    fun getResourcesByVillageId(villageId: Long):List<Resource>

    companion object {
        private const val   STMT_RESOURCES_BY_VILLAGE_ID = """
        SELECT  r.resource_id as resourceId,
                r.resource_total as resourceTotal,
                r.resource_income as resourceIncome
        FROM resources r
        JOIN resource_types rt ON rt.resource_type_id = r.resource_type_id 
        WHERE  village_id = :villageId"""
    }
}