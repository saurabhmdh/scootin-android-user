package com.scootin.network.response.orderdetail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopOwner(
    val active: Boolean,
    val deleted: Boolean,
    val email: String,
    val fcm_id: String,
    val first_name: String,
    val gstInfoReference: GstInfoReference,
    val gst_info: String,
    val id: Long,
    val mobile_number: String,
    val pan_card: String,
    val password: String
): Parcelable