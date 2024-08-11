package com.thegame.ui.controller

import com.thegame.business.model.Resource
import com.thegame.business.service.ResourceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/resource")
class ResourceRestController(private val resourceService: ResourceService) {

    @GetMapping("/getAll")
    fun getAllResources(): List<Resource> {
        return resourceService.getAllResources()
    }

    @PutMapping("/update")
    fun updateResources() {
        return resourceService.updateResourcesByVillageId()
    }
}