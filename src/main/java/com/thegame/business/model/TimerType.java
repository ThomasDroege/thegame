package com.thegame.business.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "timer_types", schema = "data")
public class TimerType {

    @Id
    @Column(name = "timer_type_id")
    private Long timerTypeId;

    @Column(name = "timer_name")
    private String timerName;

    public Long getTimerTypeId() {
        return timerTypeId;
    }

    public void setTimerTypeId(Long timerTypeId) {
        this.timerTypeId = timerTypeId;
    }

    public String getTimerName() {
        return timerName;
    }

    public void setTimerName(String timerName) {
        this.timerName = timerName;
    }
}
