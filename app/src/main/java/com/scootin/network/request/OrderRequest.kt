package com.scootin.network.request

data class OrderRequest(
    val paymentMode: String, // paymentMode
    val serviceAreaId: Long,
    val addressId: Long? = null, // addressId
    val promoCode: String? = null
)