package com.scootin.network.response.orderdetail

data class OrderDetail(
    val addressDetails: AddressDetails,
    val expressDelivery: Boolean,
    val id: Long,
    val media: Media,
    val orderDate: String,
    val order_status: String,
    val paymentDetails: PaymentDetails,
    val reviewDoneIndicator: Boolean,
    val shopManagement: ShopManagement,
    val userInfo: UserInfo
)