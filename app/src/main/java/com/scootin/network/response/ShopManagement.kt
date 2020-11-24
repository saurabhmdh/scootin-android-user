package com.scootin.network.response

data class ShopManagement(
    val address: AddressDetails,
    val closeTime: String,
    val createdAt: Long,
    val deleted: Boolean,
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val modified: Long,
    val name: String,
    val openTime: String,
    val shopLandLineNumber: String,
    val shopOwner: ShopOwner,
    val status: Boolean
)