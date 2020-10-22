package com.scootin.network.response.wallet

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class WalletTransactionResponse(
    val amount: Int,
    val id: Int,
    val paymentType: String,
    val transactionDate: String,
    val transactionDescription: String,
    val transactionReference: String,
    val transactionStatus: String
) : Parcelable