package com.scootin.network.request

import androidx.annotation.Keep

@Keep
data class RequestSearchWithFilter(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val query: String = "",
    val inventoryTypes: List<String> = emptyList()
)