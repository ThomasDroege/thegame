package com.thegame.business.repository;

import com.thegame.business.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query(nativeQuery = true, value = ResourceRepository.STMT_RESOURCES_BY_VILLAGE_ID_OLD)
    List<ResourceByVillageResponse> getResourcesByVillageId(@Param("villageId") Long villageId);

    @Query(nativeQuery = true, value = ResourceRepository.STMT_AGGREGATED_RESOURCES_BY_VILLAGE_ID)
    List<ResourceByVillageResponse> getAggregatedResourcesByVillageId(@Param("villageId") Long villageId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = ResourceRepository.STMT_UPDATE_RESOURCES_BY_VILLAGE_ID)
    void updateResourcesByVillageId(
            @Param("resourceTypeId") Long resourceTypeId,
            @Param("villageId") Long villageId,
            @Param("resourceAtUpdateTime") Long resourceAtUpdateTime,
            @Param("resourceIncome") Long resourceIncome,
            @Param("updateTime") LocalDateTime updateTime
    );

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = ResourceRepository.STMT_DELETE_RESOURCE_BY_UPDATETIME)
    void deleteResourceByUpdateTime(
            @Param("villageId") Long villageId,
            @Param("resourceTypeId") Long resourceTypeId,
            @Param("updateTime") LocalDateTime updateTime
    );

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = ResourceRepository.STMT_INSERT_RESOURCE_BY_VILLAGE_ID)
    void insertResourceByVillageId(
            @Param("villageId") Long villageId,
            @Param("resourceTypeId") Long resourceTypeId,
            @Param("resourceAtUpdateTime") Long resourceAtUpdateTime,
            @Param("resourceIncome") Long resourceIncome
    );

    interface ResourceByVillageResponse {
        Long getResourceTypeId();
        String getResourceName();
        Long getResourceAtUpdateTime();
        Long getResourceIncome();
        LocalDateTime getUpdateTime();
    }

    String STMT_RESOURCES_BY_VILLAGE_ID_OLD = """
        SELECT  r.resource_type_id as resourceTypeId,
                r.resource_at_update_time as resourceAtUpdateTime,
                r.resource_income as resourceIncome,
                r.update_time as updateTime,
                rt.resource_name as resourceName
        FROM data.resources r
        JOIN data.resource_types rt ON rt.resource_type_id = r.resource_type_id 
        WHERE  village_id = :villageId""";

    String STMT_AGGREGATED_RESOURCES_BY_VILLAGE_ID = """
        WITH max_update_time AS (
            SELECT r.resource_type_id,
            MAX(r.update_time) AS max_update_time
            FROM data.resources r
            WHERE r.village_id = :villageId
            GROUP BY r.resource_type_id)
        SELECT r.resource_type_id AS resourceTypeId,
        COALESCE(MAX(r.resource_at_update_time), 0) + SUM(
        CASE WHEN r.update_time = mu.max_update_time THEN 0
        ELSE (EXTRACT(EPOCH FROM (mu.max_update_time - r.update_time)) / 3600) * r.resource_income
        END) AS resourceAtUpdateTime,
        MAX(r.resource_income) AS resourceIncome,
        MAX(r.update_time) AS updateTime,
        rt.resource_name AS resourceName
        FROM data.resources r
        JOIN data.resource_types rt ON rt.resource_type_id = r.resource_type_id
        JOIN max_update_time mu ON mu.resource_type_id = r.resource_type_id
        WHERE r.village_id = :villageId
        GROUP BY r.resource_type_id, rt.resource_name""";

    String STMT_UPDATE_RESOURCES_BY_VILLAGE_ID = """
        UPDATE data.resources
        SET resource_at_update_time = :resourceAtUpdateTime,
            resource_income = :resourceIncome,
            update_time = :updateTime
        WHERE resource_type_id = :resourceTypeId 
        AND village_id = :villageId""";

    String STMT_DELETE_RESOURCE_BY_UPDATETIME = """
        DELETE FROM data.resources r  
        WHERE r.village_id = :villageId
        AND r.resource_type_id = :resourceTypeId
        AND r.update_time = :updateTime""";

    String STMT_INSERT_RESOURCE_BY_VILLAGE_ID = """
        INSERT INTO data.resources 
        VALUES (nextval('thegame.data.seq_resource'),
                :villageId, 
                :resourceTypeId,
                :resourceAtUpdateTime,
                :resourceIncome,
                CURRENT_TIMESTAMP)""";
}
