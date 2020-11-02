package com.scootin.network.response.order

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    val active: Boolean,
    val createdAt: Long,
    val deleted: Boolean,
    val email: String,
    val fcmId: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val mobileNumber: String,
    val modified: Long,
    val otp: String,
    val otpExpireTime: Long,
    val password: String,
    val profilePicture: String,
    val walletInfoDetails: WalletInfoDetails
): Parcelable