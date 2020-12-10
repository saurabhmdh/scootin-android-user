package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentDetails(
    val deliveryFreeAmount: Double?,
    val amount: Double?,
    val totalGSTAmount: Double?,
    val totalAmount: Double?,
    val id: Long,
    val paymentStatus: String,
    val paymentMode: String?,
    val promoDiscount: Double?,
    val orderReference: String?,
    val promoCodeApplied: Boolean?,
    val totalSaving: Double?,
    val promoCodeID: Long?,
    val mrp: Double?
) : Parcelable