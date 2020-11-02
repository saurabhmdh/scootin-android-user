package com.scootin.network.response.inorder

data class Address(
    val addressLine1: String,
    val addressLine2: String,
    val addressType: String,
    val city: String,
    val deleted: Boolean,
    val hasDefault: Boolean,
    val id: Int,
    val pincode: String,
    val stateDetails: StateDetails,
    val userInfo: Any
)