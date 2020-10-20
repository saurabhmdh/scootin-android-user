package com.scootin.network.request

class AddToCartRequest(
    val userID: String,
    val inventoryID: String,
    val quantity: String
)