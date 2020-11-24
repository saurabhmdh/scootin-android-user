package com.scootin.network.response.citywide

import com.scootin.network.response.AddressDetails
import com.scootin.network.response.Media
import com.scootin.network.response.PaymentDetails
import com.scootin.network.response.UserInfo

data class CityWideOrderResponse(
    val PaymentDetails: PaymentDetails,
    val deliveryAddressDetails: AddressDetails,
    val id: Int,
    val media: Media,
    val orderDate: String,
    val orderStatus: String,
    val pickupAddressDetails: AddressDetails,
    val serviceArea: ServiceArea,
    val userInfo: UserInfo
) {
    data class ServiceArea(
        val deleted: Boolean,
        val id: Int,
        val latitude: Double,
        val longitude: Double,
        val name: String,
        val serviceRadius: Int
    )
}