package com.scootin.network.response.order

data class CheckOutResponse(
    val amount: Double,
    val deliveryFreeAmount: Double,
    val mrp: Double,
    val totalAmount: Double,
    val totalGSTAmount: Double,
    val totalSaving: Double,
    val couponDiscount: Double
)