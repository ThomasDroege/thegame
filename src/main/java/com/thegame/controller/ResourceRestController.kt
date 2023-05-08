package com.thegame.controller

import com.thegame.model.Resource
import com.thegame.service.ResourceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/resource")
class ResourceRestController(private val resourceService: ResourceService) {

    @GetMapping("/getAll")
    fun getAllResources(): List<Resource> {
        return resourceService.getAllResources()
    }
}