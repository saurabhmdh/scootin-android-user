package com.scootin.network.response.orderdetail

data class PaymentDetails(
    val deliveryFreeAmount: Double,
    val id: Int,
    val payment_status: String,
    val totalAmount:Double
)