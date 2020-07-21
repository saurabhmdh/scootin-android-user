package com.scootin.database.table

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "cache")
@JvmSuppressWildcards
data class Cache (
    @PrimaryKey
    var index: String,
    var value: String
)