package com.thegame.business.repository;

import java.time.LocalDateTime;

public interface ResourceByVillageResponse {
    Long getResourceTypeId();
    String getResourceName();
    Long getResourceAtUpdateTime();
    Long getResourceIncome();
    float getResourceIncomeModifier();
    LocalDateTime getUpdateTime();
    LocalDateTime getResourceIncomeUpdateTime();
    LocalDateTime getResourceIncomeModifierUpdateTime();
}
