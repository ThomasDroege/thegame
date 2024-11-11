package com.thegame.ui.controller;

import com.thegame.business.service.BuildingService;
import com.thegame.business.service.ResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
public class VillageController {

    private final ResourceService resourceService;
    private final BuildingService buildingService;

    public VillageController(ResourceService resourceService, BuildingService buildingService) {
        this.resourceService = resourceService;
        this.buildingService = buildingService;
    }

    @RequestMapping("village.html")
    public String showVillage(Model model) {
        model.addAttribute("resources", resourceService.getResourcesByVillageId(1L));
        model.addAttribute("buildings", buildingService.getBuildingsByVillageId(1L));
        model.addAttribute("timestampNow", LocalDateTime.now());
        return "village";
    }
}