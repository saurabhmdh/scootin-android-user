package com.scootin.network.response.orderdetail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GstInfoReference(
    val deleted: Boolean,
    val filename: String,
    val id: Long,
    val type: String,
    val url: String
): Parcelable