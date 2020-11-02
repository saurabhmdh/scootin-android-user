package com.scootin.database.table

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "state")
@JvmSuppressWildcards
data class State(
    val countryDetails: CountryDetails,
    @PrimaryKey
    val id: Int,
    val name: String
) {
    data class CountryDetails(
        val id: Int,
        val name: String
    )
}