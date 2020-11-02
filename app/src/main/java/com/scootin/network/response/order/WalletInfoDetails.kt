package com.scootin.network.response.order

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WalletInfoDetails(
    val balance: Double,
    val currency: String,
    val id: Int,
    val lastUpdated: Long,
    val lastUpdatedBy: String
): Parcelable