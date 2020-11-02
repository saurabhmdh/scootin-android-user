package com.scootin.network.response

import com.scootin.network.response.Address

data class ShopManagement(
    val address: Address,
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