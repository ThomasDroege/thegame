package com.thegame.business.model

import jakarta.persistence.*

@Entity
@Table(name = "resources", schema = "data")
class Resource {

    @Id
    @Column(name = "resource_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var resourceId: Long? = null

    @ManyToOne
    @JoinColumn(name = "village_id")
    var village: Village? = null

    @ManyToOne
    @JoinColumn(name = "resource_type_id")
    var resourceType: ResourceType? = null

    @Column(name = "resource_total")
    var resourceTotal: Long? = null

    @Column(name = "resource_income")
    var resourceIncome: Long? = null
}