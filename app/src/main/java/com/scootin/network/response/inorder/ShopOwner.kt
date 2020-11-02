package com.scootin.network.response.inorder

data class ShopOwner(
    val aadharCardReference: Any,
    val aadhar_card: Any,
    val active: Boolean,
    val deleted: Boolean,
    val email: String,
    val fcm_id: String,
    val first_name: String,
    val gstInfoReference: GstInfoReference,
    val gst_info: String,
    val id: Int,
    val last_name: Any,
    val mobile_number: String,
    val panReference: Any,
    val pan_card: String,
    val password: String
)