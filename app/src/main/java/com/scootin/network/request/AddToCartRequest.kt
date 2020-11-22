package com.scootin.network.request

data class AddToCartRequest(
    val userID: Int,
    val inventoryID: Int?,
    val quantity: Int
)