package com.scootin.network.response.order

data class WalletInfoDetailsX(
    val balance: Int,
    val currency: String,
    val id: Int,
    val lastUpdated: Any,
    val lastUpdatedBy: Any
)