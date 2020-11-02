package com.scootin.database.table

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "address")
@JvmSuppressWildcards
data class Address(
    @PrimaryKey
    var id: Int = 0,
    val address_line_1: String?,
    val addressType: String?,
    val city: String?,
    val pincode: String?,
    val state_id: String?
)