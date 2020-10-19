package com.scootin.network.request

import androidx.annotation.Keep

@Keep
data class RequestSearch(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val query: String = ""
)