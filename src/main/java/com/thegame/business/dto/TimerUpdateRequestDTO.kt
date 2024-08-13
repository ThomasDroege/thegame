package com.thegame.business.dto

data class TimerUpdateRequestDTO (
    val villageId: Long,
    val timerTypeId: Long,
    val objectTypeId: Long,
    val duration: Long
)