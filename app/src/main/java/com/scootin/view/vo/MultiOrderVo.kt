package com.scootin.view.vo

import com.scootin.network.response.AddressDetails

data class MultiOrderVo(
    val multiOrders: String,
    val deliveryAddress: AddressDetails,
    val amount: Double,
    val deliveryFreeAmount: Double,
    val totalGSTAmount: Double,
    val totalSaving: Double,
    val totalAmount: Double
)