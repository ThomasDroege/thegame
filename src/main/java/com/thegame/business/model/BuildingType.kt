package com.thegame.business.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "building_types", schema = "data")
class BuildingType {

    @Id
    @Column(name = "building_type_id")
    var buildingTypeId: Long? = null

    @Column(name = "building_name")
    var buildingName: String? = null

}