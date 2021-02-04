package com.scootin.network.response.orderdetail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ShopBannerReference(
    val deleted: Boolean,
    val filename: String,
    val id: Int,
    val type: String,
    val url: String
): Parcelable