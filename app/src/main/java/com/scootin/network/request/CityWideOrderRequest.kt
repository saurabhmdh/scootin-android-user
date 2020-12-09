package com.scootin.network.request

data class CityWideOrderRequest(
    val deliveryAddressDetails: Long,
    val pickupAddressDetails: Long,
    val mediaId: Long,
    val serviceAreaId: Long,
    val distance: Int
)