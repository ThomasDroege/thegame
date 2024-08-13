package com.thegame.business.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "timer_types", schema = "data")
class TimerType {

    @Id
    @Column(name = "timer_type_id")
    var timerTypeId: Long? = null

    @Column(name = "timer_name")
    var timerName: String? = null

}