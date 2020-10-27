package com.scootin.network.request

class AddToCartRequest(
    val userID: Int,
    val inventoryID: Int?,
    val quantity: Int
)