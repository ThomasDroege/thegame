package com.thegame.business.service

import com.thegame.business.dto.ResourceUpdateRequestDTO
import com.thegame.business.dto.TimerUpdateRequestDTO
import com.thegame.business.repository.TimerRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TimerService(private val timerRepository: TimerRepository) {

    fun getBuildingTimerByVillageId(villageId: Long): List<TimerRepository.BuildingTimerByVillageResponse> {
        return timerRepository.getBuildingTimerByVillageId(villageId)
    }

    fun updateBuildingTimerByVillageId(request: TimerUpdateRequestDTO) {
            val updateTime = LocalDateTime.now().plusSeconds(request.duration)
            timerRepository.updateBuildingTimerByVillageId(
                request.villageId,
                request.timerTypeId,
                request.objectTypeId,
                updateTime
            )
    }
}