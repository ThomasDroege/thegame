package com.thegame.business.dto

data class ResourceUpdateRequestDTO (
    val villageId: Long,
    val resources: List<ResourceDto>
)

data class ResourceDto(
    val resourceTypeId: Long,
    val resourceAtUpdateTime: Long?,
    val resourceIncome: Long
)