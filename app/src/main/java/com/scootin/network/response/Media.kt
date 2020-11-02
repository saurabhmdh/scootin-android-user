package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(
    val deleted: Boolean,
    val filename: String,
    val id: Int,
    val type: String,
    val url: String
): Parcelable