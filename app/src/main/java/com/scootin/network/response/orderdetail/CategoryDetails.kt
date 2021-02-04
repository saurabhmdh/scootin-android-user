package com.scootin.network.response.orderdetail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CategoryDetails(
    val active: Boolean,
    val deleted: Boolean,
    val description: String,
    val id: Int,
    val name: String
): Parcelable