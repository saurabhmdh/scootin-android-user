package com.scootin.database.table

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.libraries.places.api.model.Place
import com.scootin.extensions.orDefault


@Keep
@Entity(tableName = "entity_location")
data class EntityLocation(
    @PrimaryKey
    var id: Int = 0,

    var locationID: String,
    var locationName: String,
    var latitude: Double,
    var longitude: Double,
    var address: String
) {
    constructor(place: Place) : this(0,
        place.id.orEmpty(),
        place.name.orEmpty(),
        place.latLng?.latitude.orDefault(0.0),
        place.latLng?.longitude.orDefault(0.0),
        place.address.orEmpty())
}