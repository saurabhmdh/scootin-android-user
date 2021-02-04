package com.scootin.network.response.orderdetail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceID(
    val deleted: Boolean,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val serviceRadius: Int
): Parcelable