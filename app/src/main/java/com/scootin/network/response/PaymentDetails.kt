package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentDetails(
    val amount: Double,
    val deliveryFreeAmount: Double,
    val id: Long,
    val paymentStatus: String,
    val paymentMode: String,
    val totalAmount: Double,
    val totalGSTAmount: Double,
    val orderReference: String?
) : Parcelable