package com.scootin.network.response

data class WalletInfoDetails(
    val balance: Int,
    val currency: String,
    val id: Int,
    val lastUpdated: String,
    val lastUpdatedBy: String
)