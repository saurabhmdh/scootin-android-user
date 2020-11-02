package com.scootin.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    val active: Boolean,
    val created_date: Long,
    val deleted: Boolean,
    val fcm_id: String,
    val id: Int,
    val last_modified_date: Long,
    val mobile_number: String,
    val otp: String,
    val otp_expire_time: Long,
    val walletInfoDetails: WalletInfoDetails
): Parcelable {

    @Parcelize
    data class WalletInfoDetails(
        val balance: Int,
        val currency: String,
        val id: Int
    ): Parcelable
}