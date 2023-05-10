package com.thegame.business.service

import com.thegame.business.model.Resource
import com.thegame.business.repository.ResourceRepository
import org.springframework.stereotype.Service

@Service
class ResourceService(private val resourceRepository: ResourceRepository) {

    fun getAllResources(): List<Resource> {
        return resourceRepository.findAll()
    }
}