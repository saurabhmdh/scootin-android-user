package com.scootin.network.response.order

data class OrderHistoryItem(
    val addressDetails: AddressDetails,
    val directOrder: Boolean,
    val expressDelivery: Boolean,
    val id: Int,
    val orderDate: OrderDate,
    val orderStatus: String,
    val rejectReason: String,
    val totalAmount: Double,
    val userInfo: UserInfoX
)