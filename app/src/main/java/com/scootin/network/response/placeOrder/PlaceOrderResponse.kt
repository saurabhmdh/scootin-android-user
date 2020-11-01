package com.scootin.network.response.placeOrder

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceOrderResponse(
    val addressDetails: AddressDetails,
    val expressDelivery: Boolean,
    val id: Long,
    val orderDate: OrderDate,
    val orderStatus: String,
    val paymentDetails: PaymentDetails,
    val reviewDoneIndicator: Boolean,
    val userInfo: UserInfo
) : Parcelable {

    @Parcelize
    data class AddressDetails(
        val addressLine1: String,
        val addressLine2: String,
        val address_type: String,
        val city: String,
        val deleted: Boolean,
        val hasDefault: Boolean,
        val id: Long,
        val pincode: String,
        val stateDetails: StateDetails,
        val userInfo: UserInfo
    ) : Parcelable

    @Parcelize
    data class CountryDetails(
        val id: Long,
        val name: String
    ) : Parcelable

    @Parcelize
    data class StateDetails(
        val countryDetails: CountryDetails,
        val id: Long,
        val name: String
    ) : Parcelable

    @Parcelize
    data class UserInfo(
        val active: Boolean,
        val created_date: Long,
        val deleted: Boolean,
        val fcm_id: String,
        val id: Long,
        val last_modified_date: Long,
        val mobile_number: String,
        val otp: String,
        val otp_expire_time: Long
    ) : Parcelable

    @Parcelize
    data class PaymentDetails(
        val amount: Double,
        val deliveryFreeAmount: Double,
        val id: Long,
        val payment_status: String,
        val totalAmount: Double,
        val totalGSTAmount: Double,
        val orderReference: String?
    ) : Parcelable

    @Parcelize
    data class OrderDate(
        val nanos: Int,
        val seconds: Int
    ) : Parcelable
}