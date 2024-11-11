package com.thegame.business.dto;

public class TimerUpdateRequestDTO {

    private Long villageId;
    private Long timerTypeId;
    private Long objectTypeId;
    private Long duration;

    public Long getVillageId() {
        return villageId;
    }

    public void setVillageId(Long villageId) {
        this.villageId = villageId;
    }

    public Long getTimerTypeId() {
        return timerTypeId;
    }

    public void setTimerTypeId(Long timerTypeId) {
        this.timerTypeId = timerTypeId;
    }

    public Long getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(Long objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public TimerUpdateRequestDTO(Long villageId, Long timerTypeId, Long objectTypeId, Long duration) {
        this.villageId = villageId;
        this.timerTypeId = timerTypeId;
        this.objectTypeId = objectTypeId;
        this.duration = duration;
    }
}