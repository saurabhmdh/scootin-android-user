package com.scootin.network.response.orderdetail

import com.scootin.network.response.AddressDetails

data class ShopManagement(
    val address: AddressDetails,
    val amount: Int,
    val categoryDetails: List<CategoryDetails>,
    val closeTime: String,
    val deleted: Boolean,
    val discountType: String,
    val shopActiveForOrders: Boolean,
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val openTime: String,
    val serviceID: ServiceID,
    val shopBannerReference: ShopBannerReference,
    val shopLandLineNumber: String,
    val shopOwner: ShopOwner,
    val status: Boolean
)