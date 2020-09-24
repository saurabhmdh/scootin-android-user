package com.scootin.network.response.home

data class ResponseServiceArea(
    val deleted: Boolean,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val serviceRadius: Int
)