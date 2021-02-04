package com.scootin.network.response.cart

import android.os.Parcelable
import com.scootin.network.response.ShopManagement
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartListResponseItem(
    val deleted: Boolean,
    val hasOrdered: Boolean,
    val id: Int?,
    val inventoryDetails: InventoryDetails,
    val quantity: Int?,
    val userInfo: UserInfoX
) : Parcelable {

    @Parcelize
    data class InventoryDetails(
        val active: Boolean,
        val categoryDetails: CategoryDetails,
        val currentQuantity: Int?,
        val description: String?,
        val id: Int?,
        val price: Double,
        val productImage: ProductImage,
        val quantity: Int?,
        val serviceID: ServiceIDX,
        val shopManagement: ShopManagement,
        val title: String?,
        val discountPrice: Double?
    ) : Parcelable

    @Parcelize
    data class ServiceIDX(
        val deleted: Boolean,
        val id: Int?,
        val latitude: Double,
        val longitude: Double,
        val name: String?,
        val serviceRadius: Long
    ) : Parcelable


    @Parcelize
    data class ProductImage(
        val deleted: Boolean,
        val filename: String?,
        val id: Int?,
        val thumb: String?,
        val type: String?,
        val url: String?
    ) : Parcelable

    @Parcelize
    data class CategoryDetails(
        val active: Boolean,
        val deleted: Boolean,
        val description: String?,
        val id: Int?,
        val name: String?
    ) : Parcelable

    @Parcelize
    data class UserInfoX(
        val active: Boolean,
        val createdAt: Long,
        val deleted: Boolean,
        val email: String?,
        val fcmId: String?,
        val firstName: String?,
        val id: Int?,
        val lastName: String?,
        val mobileNumber: String?,
        val modified: Long,
        val otp: String?,
        val otpExpireTime: Long,
        val password: String?,
        val profilePicture: String?,
        val walletInfoDetails: WalletInfoDetails
    ) : Parcelable

    @Parcelize
    data class WalletInfoDetails(
        val balance: Double?,
        val currency: String?,
        val id: Int?,
        val lastUpdated: String?,
        val lastUpdatedBy: String?
    ) : Parcelable

}