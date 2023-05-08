package com.thegame.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "resource_types", schema = "data")
class ResourceType {

    @Id
    @Column(name = "resource_type_id")
    var resourceTypeId: Long? = null

    @Column(name = "resource_name")
    var resourceName: String? = null

}