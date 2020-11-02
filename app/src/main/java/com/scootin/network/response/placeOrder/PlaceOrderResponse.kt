package com.scootin.network.response.placeOrder

import android.os.Parcelable
import com.scootin.network.response.Address
import com.scootin.network.response.PaymentDetails
import com.scootin.network.response.UserInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceOrderResponse(
    val addressDetails: Address,
    val expressDelivery: Boolean,
    val id: Long,
    val orderDate: OrderDate,
    val orderStatus: String,
    val paymentDetails: PaymentDetails,
    val reviewDoneIndicator: Boolean,
    val userInfo: UserInfo
) : Parcelable {
    @Parcelize
    data class OrderDate(
        val nanos: Int,
        val seconds: Int
    ) : Parcelable
}