package com.thegame.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users", schema = "data")
class User {

    @Id
    var id: Long? = null

    var firstName: String? = null

    var lastName: String? = null
}