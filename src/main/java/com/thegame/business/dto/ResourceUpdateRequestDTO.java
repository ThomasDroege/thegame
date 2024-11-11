package com.thegame.business.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ResourceUpdateRequestDTO {

    private Long villageId;
    private List<ResourceDto> resources;

    public Long getVillageId() {
        return villageId;
    }

    public void setVillageId(Long villageId) {
        this.villageId = villageId;
    }

    public List<ResourceDto> getResources() {
        return resources;
    }

    public void setResources(List<ResourceDto> resources) {
        this.resources = resources;
    }

    public ResourceUpdateRequestDTO(Long villageId, List<ResourceDto> resources) {
        this.villageId = villageId;
        this.resources = resources;
    }
}

