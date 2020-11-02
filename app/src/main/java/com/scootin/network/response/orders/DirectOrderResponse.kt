package com.scootin.network.response.orders

import com.scootin.network.response.*


data class DirectOrderResponse(
    val addressDetails: Address,
    val expressDelivery: Boolean,
    val id: Int,
    val media: Media,
    val orderDate: OrderDate,
    val order_status: String,
    val paymentDetails: PaymentDetails,
    val reviewDoneIndicator: Boolean,
    val shopManagement: ShopManagement,
    val userInfo: UserInfo
) {
    data class OrderDate(
        val nanos: Int,
        val seconds: Int
    )
}