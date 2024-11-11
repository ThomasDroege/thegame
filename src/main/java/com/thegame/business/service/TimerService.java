package com.thegame.business.service;

import com.thegame.business.dto.TimerUpdateRequestDTO;
import com.thegame.business.repository.TimerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimerService {

    private final TimerRepository timerRepository;

    public TimerService(TimerRepository timerRepository) {
        this.timerRepository = timerRepository;
    }

    public List<TimerRepository.BuildingTimerByVillageResponse> getBuildingTimerByVillageId(Long villageId) {
        return timerRepository.getBuildingTimerByVillageId(villageId);
    }

    public void updateBuildingTimerByVillageId(TimerUpdateRequestDTO request) {
        LocalDateTime updateTime = LocalDateTime.now().plusSeconds(request.getDuration());
        timerRepository.updateBuildingTimerByVillageId(
                request.getVillageId(),
                request.getTimerTypeId(),
                request.getObjectTypeId(),
                updateTime
        );
    }
}