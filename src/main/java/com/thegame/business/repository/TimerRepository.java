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
public interface TimerRepository extends JpaRepository<Resource, Long> {

    @Query(nativeQuery = true, value = STMT_BUILDING_TIMER_BY_VILLAGE_ID)
    List<BuildingTimerByVillageResponse> getBuildingTimerByVillageId(@Param("villageId") Long villageId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = STMT_UPDATE_BUILDING_TIMER_BY_VILLAGE_ID)
    void updateBuildingTimerByVillageId(
            @Param("villageId") Long villageId,
            @Param("timerTypeId") Long timerTypeId,
            @Param("objectTypeId") Long objectTypeId,
            @Param("updateTime") LocalDateTime updateTime
    );

    interface BuildingTimerByVillageResponse {
        Long getTimerType();
        Long getObjectTypeId();
        LocalDateTime getUpdateTime();
    }

    public static final String STMT_BUILDING_TIMER_BY_VILLAGE_ID = """
        SELECT  t.timer_type_id as timerType,
                t.object_type_id as objectTypeId,
                t.update_time as updateTime
        FROM data.timer t
        WHERE  village_id = :villageId
        AND timer_type_id = 1""";

    public static final String STMT_UPDATE_BUILDING_TIMER_BY_VILLAGE_ID = """
        UPDATE data.timer
        SET update_time = :updateTime,
            object_type_id = :objectTypeId
        WHERE timer_type_id = :timerTypeId 
        AND village_id = :villageId""";
}