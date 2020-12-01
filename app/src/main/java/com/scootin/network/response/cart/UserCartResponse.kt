package com.scootin.network.response.cart

data class UserCartResponse(
    val data : List<CartListResponseItem>,
    val price: Double
)