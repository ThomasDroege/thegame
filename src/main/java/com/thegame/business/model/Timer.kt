package com.thegame.business.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "timer", schema = "data")
class Timer {

    @Id
    @Column(name = "timer_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var timerId: Long? = null

    @ManyToOne
    @JoinColumn(name = "village_id")
    var village: Village? = null

    @ManyToOne
    @JoinColumn(name = "timer_type_id")
    var timerType: TimerType? = null

    @Column(name = "object_type_id")
    var objectTypeId: Long? = null

    @Column(name = "update_time")
    var updateTime: LocalDateTime? = null
}