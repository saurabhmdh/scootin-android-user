package com.scootin.network.response.order

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDate(
    val epochSecond: Long,
    val nano: Long
): Parcelable