package com.scootin.database.table

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey


@Keep
@Entity(tableName = "entity_location")
data class EntityLocation(
    @PrimaryKey
    var id: Int = 0,

    var locationID: Int,
    var locationName: String,
    var latitude: Double,
    var longitude: Double,
    var address: String
)