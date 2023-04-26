package com.thegame.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users", schema = "data")
class User {

    @Id
    var id: Long? = null

    @Column(name = "firstname")
    var firstName: String? = null

    @Column(name = "lastname")
    var lastName: String? = null
}