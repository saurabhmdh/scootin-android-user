package com.scootin.network.response.orderdetail

data class Address(
    val addressLine1: String,
    val addressLine2: String,
    val address_type: String,
    val city: String,
    val deleted: Boolean,
    val hasDefault: Boolean,
    val id: Int,
    val pincode: String,
    val stateDetails: StateDetails
)