package com.scootin.network.response.orders

import com.scootin.network.response.*


data class DirectOrderResponse(
    val addressDetails: AddressDetails,
    val expressDelivery: Boolean,
    val id: Int,
    val media: Media,
    val orderDate: String,
    val order_status: String,
    val paymentDetails: PaymentDetails,
    val reviewDoneIndicator: Boolean,
    val shopManagement: ShopManagement,
    val userInfo: UserInfo,
    val deliveryDetails: DeliveryDetail?
)