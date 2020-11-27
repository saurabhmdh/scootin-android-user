package com.scootin.network.request

data class OrderRequest(
    val paymentMode: String, // paymentMode
    val addressId: Long // addressId
)