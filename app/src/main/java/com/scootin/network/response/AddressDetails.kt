package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressDetails(
    val name: String?,
    val email: String?,
    val mobileNumber: String?,
    val addressLine1: String,
    val addressLine2: String?,
    val addressType: String,
    val city: String,
    val deleted: Boolean,
    val hasDefault: Boolean,
    val id: Long,
    val pincode: String,
    val stateDetails: StateDetails,
    val userInfo: UserInfo
): Parcelable