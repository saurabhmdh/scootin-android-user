package com.scootin.network.request

class AddToCartRequest(
    val userID: String?,
    val inventoryID: Int?,
    val quantity: Int
)