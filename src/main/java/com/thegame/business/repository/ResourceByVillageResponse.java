package com.thegame.business.repository;

import java.time.LocalDateTime;

public interface ResourceByVillageResponse {
    Long getResourceTypeId();
    String getResourceName();
    Long getResourceAtUpdateTime();
    Long getResourceIncome();
    LocalDateTime getUpdateTime();
}
