package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val address_line_1: String?,
    val addressType: String?,
    val city: String?,
    val pincode: String?,
    val state_id: String?
) : Parcelable