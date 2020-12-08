package com.scootin.network.request

data class OrderRequest(
    val paymentMode: String, // paymentMode
    val addressId: Long? = null, // addressId
    val promoCode: String? = null
)