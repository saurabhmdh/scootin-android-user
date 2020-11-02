package com.scootin.network.response.orderdetail

data class ShopBannerReference(
    val deleted: Boolean,
    val filename: String,
    val id: Int,
    val type: String,
    val url: String
)