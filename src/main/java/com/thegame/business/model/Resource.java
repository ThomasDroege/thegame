package com.thegame.business.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resources", schema = "data")
public class Resource {

    @Id
    @Column(name = "resource_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long resourceId;

    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;

    @ManyToOne
    @JoinColumn(name = "resource_type_id")
    private ResourceType resourceType;

    @Column(name = "resource_at_update_time")
    private Long resourceAtUpdateTime;

    @Column(name = "resource_income")
    private Long resourceIncome;

    @Column(name = "resource_income_modifier")
    private float resourceIncomeModifier;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "resource_income_update_time")
    private String resourceIncomeUpdateTime;

    @Column(name = "resource_income_modifier_update_time")
    private String resourceIncomeModifierUpdateTime;

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceAtUpdateTime() {
        return resourceAtUpdateTime;
    }

    public void setResourceAtUpdateTime(Long resourceAtUpdateTime) {
        this.resourceAtUpdateTime = resourceAtUpdateTime;
    }

    public Long getResourceIncome() {
        return resourceIncome;
    }

    public void setResourceIncome(Long resourceIncome) {
        this.resourceIncome = resourceIncome;
    }

    public float getResourceIncomeModifier() {
        return resourceIncomeModifier;
    }

    public void setResourceIncomeModifier(float resourceIncomeModifier) {
        this.resourceIncomeModifier = resourceIncomeModifier;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getResourceIncomeUpdateTime() {
        return resourceIncomeUpdateTime;
    }

    public void setResourceIncomeUpdateTime(String resourceIncomeUpdateTime) {
        this.resourceIncomeUpdateTime = resourceIncomeUpdateTime;
    }

    public String getResourceIncomeModifierUpdateTime() {
        return resourceIncomeModifierUpdateTime;
    }

    public void setResourceIncomeModifierUpdateTime(String resourceIncomeModifierUpdateTime) {
        this.resourceIncomeModifierUpdateTime = resourceIncomeModifierUpdateTime;
    }
}