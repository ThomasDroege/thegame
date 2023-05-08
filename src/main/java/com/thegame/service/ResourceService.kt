package com.thegame.service

import com.thegame.repository.ResourceRepository
import org.springframework.stereotype.Service

@Service
class ResourceService(private val resourceRepository: ResourceRepository) {

    fun getAllResources(): List<com.thegame.model.Resource> {
        return resourceRepository.findAll();
    }
}