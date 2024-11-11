package com.thegame.ui.controller;

import com.thegame.business.dto.ResourceUpdateRequestDTO;
import com.thegame.business.model.Resource;
import com.thegame.business.service.ResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceRestController {

    private final ResourceService resourceService;

    public ResourceRestController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/getAll")
    public List<Resource> getAllResources() {
        return resourceService.getAllResources();
    }

    @PutMapping("/update")
    public void updateResources(@RequestBody ResourceUpdateRequestDTO request) {
        resourceService.updateResourcesByVillageId(request);
    }
}