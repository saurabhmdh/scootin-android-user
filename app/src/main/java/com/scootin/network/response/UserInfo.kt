package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    val fcmId: String,
    val firstName: String,
    val id: Long,
    val mobileNumber: String,
    val dateOfBirth: String,
    val gender: String,
    val city: String
): Parcelable