package com.scootin.network.response.citywide

import com.scootin.network.response.*

data class CityWideOrderResponse(
    val paymentDetails: PaymentDetails,
    val deliveryAddressDetails: AddressDetails,
    val id: Int,
    val media: Media,
    val orderDate: String,
    val orderStatus: String,
    val pickupAddressDetails: AddressDetails,
    val serviceArea: ServiceArea,
    val userInfo: UserInfo,
    val deliveryDetails: DeliveryDetail?
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