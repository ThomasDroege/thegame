package com.thegame.repository

import com.thegame.model.Resource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ResourceRepository: JpaRepository<Resource, Long> {
}