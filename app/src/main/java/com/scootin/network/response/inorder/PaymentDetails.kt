package com.scootin.network.response.inorder

data class PaymentDetails(
    val amount: Double,
    val deliveryFreeAmount: Int,
    val id: Int,
    val orderReference: String,
    val paymentMode: String,
    val paymentReferer: Any,
    val paymentStatus: String,
    val promoCodeApplied: Any,
    val promoCodeID: Any,
    val totalAmount: Double,
    val totalGSTAmount: Int,
    val transactionReference: Any,
    val usedWalletAmount: Any
)