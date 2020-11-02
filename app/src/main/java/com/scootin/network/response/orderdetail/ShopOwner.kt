package com.scootin.network.response.orderdetail

data class ShopOwner(
    val active: Boolean,
    val deleted: Boolean,
    val email: String,
    val fcm_id: String,
    val first_name: String,
    val gstInfoReference: GstInfoReference,
    val gst_info: String,
    val id: Int,
    val mobile_number: String,
    val pan_card: String,
    val password: String
)