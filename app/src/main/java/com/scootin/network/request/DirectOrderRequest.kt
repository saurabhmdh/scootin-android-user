package com.scootin.network.request

data class DirectOrderRequest(
    val addressId: Long,
    val expressDelivery: Boolean,
    val serviceAreaId: Long,
    val mediaId: Long,
    val shopId: Long,
    val extraData: String? = null
)