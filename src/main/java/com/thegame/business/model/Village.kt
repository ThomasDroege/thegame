package com.thegame.business.model

import jakarta.persistence.*

@Entity
@Table(name = "villages", schema = "data")
class Village {

    @Id
    @Column(name = "village_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var villageId: Long? = null

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null

    @Column(name = "x_coords")
    var xCoords: Long? = null

    @Column(name = "y_coords")
    var yCoords: Long? = null
}