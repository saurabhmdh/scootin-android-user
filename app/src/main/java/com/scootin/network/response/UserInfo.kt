package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    val active: Boolean,
    val deleted: Boolean,
    val email: String,
    val fcmId: String,
    val firstName: String,
    val id: Long,
    val lastName: String,
    val mobileNumber: String,
    val password: String,
    val profilePicture: String
): Parcelable