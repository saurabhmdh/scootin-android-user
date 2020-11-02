package com.scootin.network.response.orderdetail

data class WalletInfoDetails(
    val balance: Int,
    val currency: String,
    val id: Int
)