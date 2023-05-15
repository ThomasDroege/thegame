package com.thegame.business.model

import jakarta.persistence.*

@Entity
@Table(name = "buildings", schema = "data")
class Building {

    @Id
    @Column(name = "building_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var buildingId: Long? = null

    @ManyToOne
    @JoinColumn(name = "village_id")
    var village: Village? = null

    @ManyToOne
    @JoinColumn(name = "building_type_id")
    var buildingType: BuildingType? = null

    @Column(name = "building_level")
    var buildingLevel: Long? = null
}