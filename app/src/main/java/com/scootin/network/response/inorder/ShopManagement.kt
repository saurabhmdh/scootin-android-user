package com.scootin.network.response.inorder

import com.scootin.network.response.AddressDetails

data class ShopManagement(
    val address: AddressDetails,
    val amount: Int,
    val categoryDetails: CategoryDetails,
    val closeTime: String,
    val createdAt: Long,
    val deleted: Boolean,
    val discountType: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val modified: Long,
    val name: String,
    val openTime: String,
    val serviceID: ServiceID,
    val shopBannerReference: ShopBannerReference,
    val shopLandLineNumber: String,
    val shopOwner: ShopOwner,
    val status: Boolean
)