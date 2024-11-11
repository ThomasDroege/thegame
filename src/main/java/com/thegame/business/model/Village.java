package com.thegame.business.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "villages", schema = "data")
public class Village {

    @Id
    @Column(name = "village_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long villageId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "x_coords")
    private Long xCoords;

    @Column(name = "y_coords")
    private Long yCoords;

    // Getter und Setter
    public Long getVillageId() {
        return villageId;
    }

    public void setVillageId(Long villageId) {
        this.villageId = villageId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getXCoords() {
        return xCoords;
    }

    public void setXCoords(Long xCoords) {
        this.xCoords = xCoords;
    }

    public Long getYCoords() {
        return yCoords;
    }

    public void setYCoords(Long yCoords) {
        this.yCoords = yCoords;
    }
}
