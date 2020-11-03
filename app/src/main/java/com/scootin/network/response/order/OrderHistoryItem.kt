package com.scootin.network.response.order

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderHistoryItem(
    val addressDetails: AddressDetails,
    val directOrder: Boolean,
    val expressDelivery: Boolean,
    val id: Int,
    val orderDate: String,
    val orderStatus: String,
    val rejectReason: String,
    val totalAmount: Double,
    val userInfo: UserInfo
):Parcelable