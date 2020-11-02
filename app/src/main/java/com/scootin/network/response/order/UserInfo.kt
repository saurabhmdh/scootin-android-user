package com.scootin.network.response.order

data class UserInfo(
    val active: Boolean,
    val createdAt: Long,
    val deleted: Boolean,
    val email: Any,
    val fcmId: String,
    val firstName: Any,
    val id: Int,
    val lastName: Any,
    val mobileNumber: String,
    val modified: Long,
    val otp: String,
    val otpExpireTime: Long,
    val password: Any,
    val profilePicture: Any,
    val walletInfoDetails: WalletInfoDetails
)