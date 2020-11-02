package com.scootin.network.response.wallet


import com.scootin.network.response.UserInfo


data class WalletTransactionResponse(
    val amount: Double,
    val id: Long,
    val orderReference: String,
    val paymentType: String,
    val transactionDate: String,
    val userInfo: UserInfo
)