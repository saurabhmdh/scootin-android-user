package com.scootin.network.request

data class AddAddressRequest(
    val addressType: String,
    val address_line_1: String,
    val city: String,
    val has_default: Boolean,
    val pincode: String,
    val state_id: Int,
    val id: Long = -1
)