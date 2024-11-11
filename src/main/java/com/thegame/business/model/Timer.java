package com.thegame.business.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "timer", schema = "data")
public class Timer {

    @Id
    @Column(name = "timer_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long timerId;

    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;

    @ManyToOne
    @JoinColumn(name = "timer_type_id")
    private TimerType timerType;

    @Column(name = "object_type_id")
    private Long objectTypeId;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public TimerType getTimerType() {
        return timerType;
    }

    public void setTimerType(TimerType timerType) {
        this.timerType = timerType;
    }

    public Long getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(Long objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}