package com.scootin.network.response

import android.os.Parcelable
import com.scootin.network.response.orderdetail.CategoryDetails
import com.scootin.network.response.orderdetail.ServiceID
import com.scootin.network.response.orderdetail.ShopBannerReference
import com.scootin.network.response.orderdetail.ShopOwner
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopManagement(
    val address: AddressDetails,
    val amount: Int,
    val categoryDetails: List<CategoryDetails>,
    val closeTime: String,
    val deleted: Boolean,
    val discountType: String,
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val openTime: String,
    val serviceID: ServiceID,
    val shopBannerReference: ShopBannerReference,
    val shopLandLineNumber: String,
    val shopOwner: ShopOwner,
    val status: Boolean,
    val shopActiveForOrders: Boolean
): Parcelable