package com.thegame.business.service

import com.thegame.business.model.Resource
import com.thegame.business.repository.ResourceRepository
import com.thegame.business.repository.TimerRepository
import org.springframework.stereotype.Service

@Service
class TimerService(private val timerRepository: TimerRepository) {

    fun getBuildingTimerByVillageId(villageId: Long): List<TimerRepository.BuildingTimerByVillageResponse> {
        return timerRepository.getBuildingTimerByVillageId(villageId)
    }

}