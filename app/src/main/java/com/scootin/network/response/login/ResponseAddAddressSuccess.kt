package com.scootin.network.response.login

data class ResponseAddAddressSuccess(
    val addressLine1: String,
    val addressType: String,
    val city: String,
    val hasDefault: Boolean,
    val id: Int,
    val pincode: String,
    val stateDetailsId: Int
)