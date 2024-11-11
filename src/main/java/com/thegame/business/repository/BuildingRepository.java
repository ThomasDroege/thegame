package com.thegame.business.repository;

import com.thegame.business.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

    @Query(nativeQuery = true, value = BuildingRepository.STMT_BUILDINGS_BY_VILLAGE_ID)
    List<BuildingsByVillageIdResponse> getBuildingsByVillageId(Long villageId);

    @Query(nativeQuery = true, value = BuildingRepository.STMT_BUILDING_BY_VILLAGE_ID_AND_BUILDING_ID)
    Building getBuildingByVillageIdAndBuildingId(Long villageId, Long buildingTypeId);

    interface BuildingsByVillageIdResponse {
        Long getBuildingId();
        Long getBuildingTypeId();
        String getBuildingName();
        Long getBuildingLevel();
        LocalDateTime getUpdateTime();
    }

    String STMT_BUILDINGS_BY_VILLAGE_ID = """
        SELECT  b.building_id as buildingId,
                b.building_type_id as buildingTypeId,
                bt.building_name as buildingName,
                b.building_level as buildingLevel,
                b.update_time as updateTime
        FROM data.buildings b
        JOIN data.building_types bt ON bt.building_type_id = b.building_type_id 
        WHERE  village_id = :villageId""";

    String STMT_BUILDING_BY_VILLAGE_ID_AND_BUILDING_ID = """
        SELECT building_id,
               village_id,
               building_type_id,
               building_level,
               update_time
        FROM data.buildings b
        WHERE village_id = :villageId
        AND building_type_id = :buildingTypeId""";
}