package com.scootin.network.response.orderdetail

data class OrderDetail(
    val addressDetails: AddressDetails,
    val expressDelivery: Boolean,
    val id: Long,
    val media: Media,
    val orderDate: String,
    val orderStatus: String,
    val paymentDetails: PaymentDetails,
    val reviewDoneIndicator: Boolean,
    val extraData:String,
    val shopManagement: ShopManagement,
    val userInfo: UserInfo

)