package com.scootin.network.response.order

data class AddressDetails(
    val addressLine1: String,
    val addressLine2: String,
    val addressType: String,
    val city: String,
    val deleted: Boolean,
    val hasDefault: Boolean,
    val id: Int,
    val pincode: String,
    val stateDetails: StateDetails,
    val userInfo: UserInfo
)