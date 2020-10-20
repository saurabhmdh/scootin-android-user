package com.scootin.network.request

class PlaceOrderRequest(
    val address: Address,
    val expressDelivery: Boolean
) {
    class Address(
        val addressType: String,
        val address_line_1: String,
        val address_line_2: String,
        val city: String,
        val id: String,
        val pincode: String,
        val state_id: String,
        val userInfoId: String
    )
}