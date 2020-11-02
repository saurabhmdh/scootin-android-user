package com.scootin.network.response.inorder

data class WalletInfoDetails(
    val balance: Int,
    val currency: String,
    val id: Int,
    val lastUpdated: Long,
    val lastUpdatedBy: String
)