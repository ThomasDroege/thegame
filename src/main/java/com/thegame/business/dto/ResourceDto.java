package com.thegame.business.dto;

import java.time.LocalDateTime;

public class ResourceDto {

    private Long resourceTypeId;
    private Long resourceAtUpdateTime;
    private Long resourceIncome;
    private LocalDateTime updateTime;

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public Long getResourceAtUpdateTime() {
        return resourceAtUpdateTime;
    }

    public void setResourceAtUpdateTime(Long resourceAtUpdateTime) {
        this.resourceAtUpdateTime = resourceAtUpdateTime;
    }

    public Long getResourceIncome() {
        return resourceIncome;
    }

    public void setResourceIncome(Long resourceIncome) {
        this.resourceIncome = resourceIncome;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public ResourceDto(Long resourceTypeId, Long resourceAtUpdateTime, Long resourceIncome, LocalDateTime updateTime) {
        this.resourceTypeId = resourceTypeId;
        this.resourceAtUpdateTime = resourceAtUpdateTime;
        this.resourceIncome = resourceIncome;
        this.updateTime = updateTime;
    }
}
