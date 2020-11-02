package com.scootin.network.response.orderdetail

data class ServiceID(
    val deleted: Boolean,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val serviceRadius: Int
)