package com.thegame.ui.controller;


import com.thegame.business.service.BuildingService;
import com.thegame.business.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/village")
public class VillageRestController {

    private final ResourceService resourceService;
    private final BuildingService buildingService;

    @Autowired
    public VillageRestController(ResourceService resourceService, BuildingService buildingService) {
        this.resourceService = resourceService;
        this.buildingService = buildingService;
    }

    @GetMapping("/{villageId}/data")
    public Map<String, Object> getVillageData(@PathVariable Long villageId) {
        Map<String, Object> response = new HashMap<>();
        response.put("resources", resourceService.getResourcesByVillageId(villageId));
        response.put("buildings", buildingService.getBuildingsByVillageId(villageId));
        response.put("timestampNow", LocalDateTime.now());
        return response;
    }

    @PostMapping("/increaseBuildingLevel")
    public void increaseBuildingLevel(@RequestParam Long villageId, @RequestParam Long buildingTypeId) {
        buildingService.increaseBuildingLevel(villageId, buildingTypeId);
    }

}


